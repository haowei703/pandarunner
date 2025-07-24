package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.CartItemDTO;
import com.shiroha.pandarunner.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车项模块")
@RestController
@RequestMapping("/cart/item")
@AllArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * 添加购物车项
     *
     * @param cartItemDTO 购物车项
     */
    @Operation(summary = "添加购物车项")
    @PostMapping("save")
    public R<Long> save(@RequestParam(name = "cart_id") Long cartId, @Validated (ValidationGroups.Create.class) @RequestBody CartItemDTO cartItemDTO) {
        return R.ok(cartItemService.saveItem(cartId, cartItemDTO));
    }

    /**
     * 删除购物车项
     *
     * @param cartItemId 购物车项ID
     */
    @DeleteMapping("remove/{id}")
    public R<?> remove(@PathVariable("id") Long cartItemId) {
        cartItemService.removeItemById(cartItemId);
        return R.ok();
    }

    /**
     * 更新购物车商品数量
     *
     * @param itemId 购物车项ID
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @Operation(summary = "更新购物车商品数量")
    @PutMapping("update/{id}")
    public R<?> update(@PathVariable("id") Long itemId,
                       @Min(value = 0, message = "数量不能小于0")
                       @Max(value = 20, message = "数量不能大于20")
                       @RequestParam(name = "quantity")  int quantity,
                       @SaLoginId Long userId) {
        cartItemService.updateItemsQuantityById(userId, itemId, quantity);
        return R.ok();
    }

}
