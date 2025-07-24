package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.ProductDTO;
import com.shiroha.pandarunner.domain.entity.Product;
import com.shiroha.pandarunner.domain.vo.ProductVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface ProductService extends IService<Product> {

    /**
     * 新增商品
     *
     * @param userId     用户ID
     * @param productDTO 商品信息DTO
     * @return           商品ID
     */
    Long saveProduct(Long userId, ProductDTO productDTO);

    /**
     * 更新商品
     *
     * @param userId        用户ID
     * @param productId     商品ID
     * @param productDTO    商品信息DTO
     */
    void updateProduct(Long userId, Long productId, ProductDTO productDTO);

    /**
     * 删除商品
     * @param userId        用户ID
     * @param productId     商品ID
     */
    void removeProduct(Long userId, Long productId);

    /**
     * 批量查询商品
     *
     * @param productIds    ID列表
     * @return              商品列表
     */
    List<ProductVO> getProductsByIds(List<Long> productIds);

    /**
     * 获取商家商品列表
     * @param merchantId 商家ID
     * @param priorityOnly 是否仅返回优先展示的商品
     * @return 商品列表
     */
    List<ProductVO> getProductsByMerchantId(Long merchantId, boolean priorityOnly);

    /**
     * 批量获取商家商品列表
     *
     * @param merchantIds 商家ID列表
     * @return 商家ID-商品列表的映射
     */
    Map<String, List<ProductVO>> getProductsByMerchantIds(List<Long> merchantIds);

    /**
     * 批量获取分类下的商品信息
     *
     * @param categoryIds 分类ID列表
     * @return 分类ID-商品列表的映射
     */
    Map<String, List<ProductVO>> getProductsByCategoryIds(List<Long> categoryIds);

    /**
     * 验证商品属于指定商家
     *
     * @param merchantId 商家ID
     * @param products   商品列表
     */
    void validateProductsBelongToMerchant(Long merchantId, Collection<Product> products);
}
