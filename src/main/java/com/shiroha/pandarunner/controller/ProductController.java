package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.ProductDTO;
import com.shiroha.pandarunner.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品模块")
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 新增商品
     *
     * @param productDTO    商品信息
     */
    @Operation(summary = "新增商品")
    @PostMapping("save")
    public R<Long> save(@Validated(ValidationGroups.Create.class) @RequestBody ProductDTO productDTO,
                        @SaLoginId Long userId) {
        return R.ok(productService.saveProduct(userId, productDTO));
    }

    /**
     * 更新商品信息
     * @param productId     商品ID
     * @param productDTO    商品信息
     */
    @Operation(summary = "更新商品信息")
    @PutMapping("update/{id}")
    public R<?> update(@PathVariable("id") Long productId,
                       @Validated(ValidationGroups.Update.class) @RequestBody ProductDTO productDTO,
                       @SaLoginId Long userId) {
        productService.updateProduct(userId, productId, productDTO);
        return R.ok();
    }

    /**
     * 删除商品
     *
     * @param productId     商品ID
     * @param userId        用户ID
     */
    @DeleteMapping("remove/{id}")
    public R<?> remove(@PathVariable("id") Long productId, @SaLoginId Long userId) {
        productService.removeProduct(userId, productId);
        return R.ok();
    }
}
