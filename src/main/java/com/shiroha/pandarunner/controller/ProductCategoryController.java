package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.ProductCategoryDTO;
import com.shiroha.pandarunner.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品分类模块")
@RestController
@AllArgsConstructor
@RequestMapping("/product/category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    /**
     * 新增商品分类
     * @param productCategoryDTO        商品分类信息
     * @param userId                    用户ID
     * @return                          分类ID
     */
    @Operation(summary = "新增商品分类")
    @PostMapping("save")
    public R<Long> save(@Validated(ValidationGroups.Create.class) @RequestBody ProductCategoryDTO productCategoryDTO,
                        @SaLoginId Long userId) {
        return R.ok(productCategoryService.saveProductCategory(userId, productCategoryDTO));
    }

    /**
     * 更新商品分类
     *
     * @param categoryId            分类ID
     * @param productCategoryDTO    商品分类
     * @param userId                用户ID
     */
    @Operation(summary = "更新商品分类")
    @PutMapping("update/{id}")
    public R<?> update(@PathVariable("id") Long categoryId,
                       @Validated(ValidationGroups.Update.class) @RequestBody ProductCategoryDTO productCategoryDTO,
                       @SaLoginId Long userId) {
        productCategoryService.updateProductCategory(userId, categoryId, productCategoryDTO);
        return R.ok();
    }

    /**
     * 获取商家的全部商品分类和商品信息
     *
     * @param merchantId        商家ID
     */
    @Operation(summary = "获取商家的商品分类")
    @GetMapping("list")
    public R<?> list(@RequestParam(name = "merchant_id") Long merchantId) {
        return R.ok(productCategoryService.getProductCategories(merchantId));
    }

    /**
     * 删除商品分类
     *
     * @param categoryId 分类ID
     */
    @Operation(summary = "删除商品分类")
    @DeleteMapping("delete/{id}")
    public R<?> delete(@PathVariable(name = "id") Long categoryId) {
        productCategoryService.deleteProductCategory(categoryId);
        return R.ok();
    }

}
