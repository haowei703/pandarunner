package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.ProductCategoryDTO;
import com.shiroha.pandarunner.domain.entity.ProductCategory;
import com.shiroha.pandarunner.domain.vo.ProductCategoryVO;

import java.util.List;

/**
 * 商品分类表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 通过ID获取商品分类
     *
     * @param categoryId 商品分类ID
     * @return 商品分类
     */
    ProductCategory getProductCategoryById(Long categoryId);

    /**
     * 新增商品分类
     *
     * @param userId                    用户ID
     * @param productCategoryDTO        商品分类信息
     * @return                          商品分类ID
     */
    Long saveProductCategory(Long userId, ProductCategoryDTO productCategoryDTO);

    /**
     * 更新商品分类
     *
     * @param userId 用户ID
     * @param categoryId 商品分类ID
     * @param productCategoryDTO 商品分类信息
     */
    void updateProductCategory(Long userId, Long categoryId, ProductCategoryDTO productCategoryDTO);

    /**
     * 删除商品分类
     *
     * @param categoryId 商品分类ID
     */
    void deleteProductCategory(Long categoryId);

    /**
     * 获取商家的全部商品分类
     *
     * @param merchantId 商家ID
     * @return 商品分类列表
     */
    List<ProductCategoryVO> getProductCategories(Long merchantId);

    /**
     * 验证商品分类属于商家
     *
     * @param merchantId        商家ID
     * @param productCategory   商品分类
     */
    void validateProductCategory(Long merchantId, ProductCategory productCategory);

}
