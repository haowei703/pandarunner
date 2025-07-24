package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.ProductConverter;
import com.shiroha.pandarunner.domain.dto.ProductCategoryDTO;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.entity.ProductCategory;
import com.shiroha.pandarunner.domain.vo.ProductCategoryVO;
import com.shiroha.pandarunner.domain.vo.ProductVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.mapper.ProductCategoryMapper;
import com.shiroha.pandarunner.service.MerchantService;
import com.shiroha.pandarunner.service.ProductCategoryService;
import com.shiroha.pandarunner.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.shiroha.pandarunner.domain.entity.table.ProductCategoryTableDef.PRODUCT_CATEGORY;
import static com.shiroha.pandarunner.domain.entity.table.ProductTableDef.PRODUCT;

/**
 * 商品分类表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@CacheConfig(cacheNames = "productCategory")
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService{

    private final MerchantService merchantService;
    private final ProductService productService;
    private final ProductConverter converter;

    @Autowired
    public ProductCategoryServiceImpl(MerchantService merchantService,
                                      @Lazy ProductService productService,
                                      ProductConverter productConverter) {
        this.merchantService = merchantService;
        this.productService = productService;
        this.converter = productConverter;
    }

    /**
     * 通过ID获取商品分类
     *
     * @param categoryId 商品分类ID
     * @return 商品分类
     */
    @Override
    public ProductCategory getProductCategoryById(Long categoryId) {
        ProductCategory category = getById(categoryId);
        if(category == null) {
            throw new BusinessDataNotFoundException("商品分类ID不存在", categoryId);
        }
        return category;
    }

    /**
     * 新增商品分类
     *
     * @param userId             用户ID
     * @param productCategoryDTO 商品分类信息
     * @return 商品分类ID
     */
    @Override
    @CacheEvict(allEntries = true)
    public Long saveProductCategory(Long userId, ProductCategoryDTO productCategoryDTO) {
        // 检验商家ID属于用户
        Long merchantId = productCategoryDTO.getMerchantId();
        Merchant merchant = merchantService.getMerchantById(merchantId, false);
        merchantService.validateMerchantBelongToUser(userId, merchant);
        ProductCategory productCategory = converter.categoryDtoToCategory(productCategoryDTO);
        productCategory.setMerchantId(merchantId);

        // 保存数据
        save(productCategory);
        return productCategory.getId();
    }

    /**
     * 更新商品分类
     *
     * @param userId 用户ID
     * @param categoryId 商品分类ID
     * @param productCategoryDTO 商品分类信息
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateProductCategory(Long userId, Long categoryId, ProductCategoryDTO productCategoryDTO) {
        // 检验商家ID属于用户
        Long merchantId = productCategoryDTO.getMerchantId();
        Merchant merchant = merchantService.getMerchantById(merchantId, false);
        merchantService.validateMerchantBelongToUser(userId, merchant);
        ProductCategory productCategory = converter.categoryDtoToCategory(productCategoryDTO);
        productCategory.setId(categoryId);
        productCategory.setMerchantId(merchantId);

        // 更新数据
        updateById(productCategory);
    }

    /**
     * 删除商品分类
     *
     * @param categoryId 商品分类ID
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteProductCategory(Long categoryId) {
        // 删除分类
        removeById(categoryId);
        // 删除该分类下商品
        productService.remove(productService.query().where(PRODUCT.CATEGORY_ID.eq(categoryId)));
    }

    /**
     * 获取商家的全部商品分类
     *
     * @param merchantId 商家ID
     * @return 商品分类列表
     */
    @Override
    @Cacheable(key = "#merchantId", unless = "#result.isEmpty()")
    public List<ProductCategoryVO> getProductCategories(Long merchantId) {
        // 确保商家存在
        merchantService.getMerchantById(merchantId, false);

        // 获取到商品分类列表
        List<ProductCategory> categories = list(query().where(PRODUCT_CATEGORY.MERCHANT_ID.eq(merchantId)));

        if(categories.isEmpty()) {
            return List.of();
        }

        // 收集商品分类ID
        List<Long> categoriesIds = categories.stream().map(ProductCategory::getId).toList();

        // 批量查询商品
        Map<String, List<ProductVO>> productsByCategoryIds = productService.getProductsByCategoryIds(categoriesIds);

        // 初始化容器
        List<ProductCategoryVO> productCategoryVOS = new ArrayList<>(categories.size());
        for (ProductCategory category : categories) {
            // 转换为VO对象
            ProductCategoryVO productCategoryVO = converter.productCategoryToCategoryVO(category);
            // 填充数据
            productCategoryVO.setProducts(productsByCategoryIds.getOrDefault(String.valueOf(category.getId()), new ArrayList<>()));
            productCategoryVOS.add(productCategoryVO);
        }

        return productCategoryVOS;
    }

    /**
     * 验证商品分类属于商家
     *
     * @param merchantId      商家ID
     * @param productCategory 商品分类
     */
    @Override
    public void validateProductCategory(Long merchantId, ProductCategory productCategory) {
        // 检验地址属于用户
        if(!productCategory.getMerchantId().equals(merchantId)) {
            throw new BusinessDataConsistencyException(
                    String.format("商品分类[%s]不属于商家[%s]", productCategory.getId(), merchantId),
                    productCategory.getId(),
                    merchantId,
                    BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }
    }
}
