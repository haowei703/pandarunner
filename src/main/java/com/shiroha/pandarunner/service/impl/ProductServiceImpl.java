package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.ProductConverter;
import com.shiroha.pandarunner.domain.dto.ProductDTO;
import com.shiroha.pandarunner.domain.dto.SpecGroupDTO;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.entity.Product;
import com.shiroha.pandarunner.domain.entity.ProductCategory;
import com.shiroha.pandarunner.domain.vo.ProductVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.UserNotPermissionException;
import com.shiroha.pandarunner.mapper.MerchantMapper;
import com.shiroha.pandarunner.mapper.ProductMapper;
import com.shiroha.pandarunner.service.ProductCategoryService;
import com.shiroha.pandarunner.service.ProductService;
import com.shiroha.pandarunner.service.SpecGroupService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.shiroha.pandarunner.domain.entity.table.MerchantTableDef.MERCHANT;
import static com.shiroha.pandarunner.domain.entity.table.ProductTableDef.PRODUCT;

/**
 * 商品表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "product")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private final ProductConverter productConverter;
    private final MerchantMapper merchantMapper;
    private final ProductCategoryService productCategoryService;
    private final SpecGroupService specGroupService;

    /**
     * 新增商品
     *
     * @param userId     用户ID
     * @param productDTO 商品信息DTO
     * @return           商品ID
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "productCategory", allEntries = true),
                    @CacheEvict(cacheNames = "productsByMerchantIds", allEntries = true)
            }
    )
    public Long saveProduct(Long userId, ProductDTO productDTO) {
        Long merchantId = productDTO.getMerchantId();
        Long categoryId = productDTO.getCategoryId();

        // 验证商家属于用户
        isExisted(merchantId, userId);

        // 首先保存商品
        ProductCategory productCategory = productCategoryService.getProductCategoryById(categoryId);
        productCategoryService.validateProductCategory(merchantId, productCategory);
        Product product = productConverter.productDtoToProduct(productDTO);
        save(product);

        Long productId = product.getId();

        // 规格信息不为空则保存商品规格信息
        List<SpecGroupDTO> groups = productDTO.getSpecs();
        // 批量保存
        if(groups != null && !groups.isEmpty()) {
            specGroupService.saveSpecGroupBatch(userId, productId, groups);
        }
        return product.getId();
    }

    /**
     * 更新商品
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param productDTO 商品信息DTO
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "productCategory", allEntries = true),
                    @CacheEvict(cacheNames = "productsByMerchantIds", allEntries = true)
            }
    )
    public void updateProduct(Long userId, Long productId, ProductDTO productDTO) {
        Long merchantId = productDTO.getMerchantId();

        // 验证商家属于用户
        isExisted(merchantId, userId);

        // 首先更新商品
        boolean isExisted =  queryChain()
                .where(PRODUCT.MERCHANT_ID.eq(merchantId))
                .and(PRODUCT.ID.eq(productId)).exists();
        if(!isExisted) {
            throw new BusinessDataConsistencyException("产品不属于商家", productId, merchantId, BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }

        Product product = productConverter.productDtoToProduct(productDTO);
        product.setId(productId);
        updateById(product);
    }

    /**
     * 删除商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "productCategory", allEntries = true),
                    @CacheEvict(cacheNames = "productsByMerchantIds", allEntries = true)
            }
    )
    public void removeProduct(Long userId, Long productId) {
        Merchant merchant = getMerchantByUserId(userId);

        Product product = getOne(query()
                .where(PRODUCT.ID.eq(productId))
                .and(PRODUCT.MERCHANT_ID.eq(merchant.getId())));
        if(product != null) {
            removeById(productId);
        }
        throw new UserNotPermissionException("没有权限");
    }

    /**
     * 私有方法，避免直接依赖MerchantService，减少循环依赖
     */
    private void isExisted(Long merchantId, Long userId) {
        boolean isExisted = QueryChain.of(merchantMapper)
                .where(MERCHANT.ID.eq(merchantId))
                .and(MERCHANT.USER_ID.eq(userId))
                .and(MERCHANT.STATUS.eq(1))
                .exists();
        if(!isExisted) {
            throw new BusinessDataConsistencyException(
                    String.format("商家[%s]不属于用户[%s]", merchantId, userId),
                    merchantId,
                    userId,
                    BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }
    }

    private Merchant getMerchantByUserId(Long userId) {
        Merchant merchant = QueryChain.of(merchantMapper)
                .where(MERCHANT.USER_ID.eq(userId))
                .and(MERCHANT.STATUS.eq(1))
                .one();
        if(merchant == null) {
            throw new BusinessDataNotFoundException("用户ID不存在", userId);
        }

        return merchant;
    }

    /**
     * 批量查询商品
     *
     * @param productIds ID列表
     * @return 商品列表
     */
    @Override
    public List<ProductVO> getProductsByIds(List<Long> productIds) {
        List<Product> products = list(query().where(PRODUCT.ID.in(productIds)));
        return productConverter.productListToProductVOList(products);
    }

    /**
     * 获取商家商品列表
     *
     * @param merchantId   商家ID
     * @param priorityOnly 是否仅返回优先展示的商品
     * @return 商品列表
     */
    @Override
    public List<ProductVO> getProductsByMerchantId(Long merchantId, boolean priorityOnly) {
        QueryWrapper query;
        if(priorityOnly) {
            query = query()
                    .where(PRODUCT.MERCHANT_ID.eq(merchantId)
                    .and(PRODUCT.IS_PRIORITY.eq(true))
                    .and(PRODUCT.STOCK.eq(-1).or(PRODUCT.STOCK.gt(0))));
        } else {
            query = query()
                    .where(PRODUCT.MERCHANT_ID.eq(merchantId)
                    .and(PRODUCT.STOCK.eq(-1).or(PRODUCT.STOCK.gt(0))));
        }

        List<Product> products = list(query);
        return productConverter.productListToProductVOList(products);
    }

    /**
     * 批量获取商家商品列表
     *
     * @param merchantIds 商家ID列表
     * @return 商家ID-商品列表的映射
     */
    @Override
    @Cacheable(
            cacheNames = "productsByMerchantIds",
            keyGenerator = "ArrayKeyGenerator"
    )
    public Map<String, List<ProductVO>> getProductsByMerchantIds(List<Long> merchantIds) {
        //初始化map，预填充key和空列表
        Map<String, List<ProductVO>> productsByMerchantId = new HashMap<>((int) (merchantIds.size() / 0.75f));
        for(Long merchantId : merchantIds) {
            productsByMerchantId.put(String.valueOf(merchantId), new ArrayList<>());
        }

        //遍历批量查询结果，将product插入到对应列表
        list(query()
            .where(PRODUCT.MERCHANT_ID.in(merchantIds))
            .orderBy(PRODUCT.IS_PRIORITY.desc())  // 优先展示isPriority为true的商品
            .limit(10)  // 每个商家最多取10条
        ).forEach(product -> {
            List<ProductVO> products = productsByMerchantId.get(String.valueOf(product.getMerchantId()));
            products.add(productConverter.productToProductVO(product));
        });

        return productsByMerchantId;
    }

    /**
     * 批量获取分类下的商品信息
     *
     * @param categoryIds 分类ID列表
     * @return 分类ID-商品列表的映射
     */
    @Override
    public Map<String, List<ProductVO>> getProductsByCategoryIds(List<Long> categoryIds) {
        //初始化map，预填充key和空列表
        Map<String, List<ProductVO>> productsByCategoryId = new HashMap<>((int) (categoryIds.size() / 0.75f));
        for(Long categoryId : categoryIds) {
            productsByCategoryId.put(String.valueOf(categoryId), new ArrayList<>());
        }

        // 遍历批量查询结果，将product插入到对应列表
        list(query()
            .where(PRODUCT.CATEGORY_ID.in(categoryIds))
            .orderBy(PRODUCT.CATEGORY_ID.desc())    // 按分类ID排序确保有序性
        ).forEach(product -> {
            List<ProductVO> products = productsByCategoryId.get(String.valueOf(product.getCategoryId()));
            products.add(productConverter.productToProductVO(product));
        });

        return productsByCategoryId;
    }

    /**
     * 验证商品属于指定商家
     *
     * @param merchantId 商家ID
     * @param products   商品列表
     */
    @Override
    public void validateProductsBelongToMerchant(Long merchantId, Collection<Product> products) {
        products.forEach(product -> {
            if (!merchantId.equals(product.getMerchantId())) {
                throw new BusinessDataConsistencyException(
                        String.format("商品[%s]不属于商家[%s]", product.getId(), merchantId),
                        product.getId(),
                        merchantId,
                        BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
            }
        });
    }
}
