package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.domain.vo.CartVO;
import com.shiroha.pandarunner.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车模块")
@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 新增购物车
     *
     * @param merchantId 商家ID
     */
    @Operation(summary = "新增购物车")
    @PostMapping("save")
    public R<Long> save(@RequestParam("merchant_id") Long merchantId, @SaLoginId Long userId) {
        return R.ok(cartService.saveCart(userId, merchantId));
    }

    /**
     * 获取用户在商家的购物车
     *
     * @param merchantId 商家ID
     * @param userId 用户ID
     */
    @Operation(summary = "获取用户在商家的购物车")
    @GetMapping
    public R<CartVO> getCartByMerchantId(@RequestParam("merchant_id") Long merchantId, @SaLoginId Long userId) {
        return R.ok(cartService.getCartByMerchantId(userId, merchantId));
    }

    /**
     * 获取用户全部购物车
     */
    @Operation(summary = "获取用户全部购物车")
    @GetMapping("list")
    public R<List<CartVO>> list(@SaLoginId Long userId) {
        return R.ok(cartService.getCartByUserId(userId));
    }

    /**
     * 清空购物车
     *
     * @param cartId 购物车ID
     */
    @Operation(summary = "清空购物车")
    @DeleteMapping("remove/{id}")
    public R<?> remove(@PathVariable("id") Long cartId, @SaLoginId Long userId) {
        cartService.removeCartByCartId(userId, cartId);
        return R.ok();
    }

}
