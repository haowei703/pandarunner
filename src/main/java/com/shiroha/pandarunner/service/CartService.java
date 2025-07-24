package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.entity.Cart;
import com.shiroha.pandarunner.domain.vo.CartItemVO;
import com.shiroha.pandarunner.domain.vo.CartVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface CartService extends IService<Cart> {

    /**
     * 新增用户购物车
     *
     * @param userId    用户ID
     * @param merchantId    商家ID
     * @return      购物车ID
     */
    Long saveCart(Long userId, Long merchantId);

    /**
     * 获取用户在指定商家的购物车（含商品项）
     *
     * @param userId 用户ID
     * @param merchantId 商家ID
     * @return 购物车信息
     */
    CartVO getCartByMerchantId(Long userId, Long merchantId);

    /**
     * 获取用户全部购物车内容
     *
     * @param userId    用户ID
     * @return  购物车信息列表
     */
    List<CartVO> getCartByUserId(Long userId);

    /**
     * 清空指定购物车中的全部商品
     *
     * @param userId 用户ID
     * @param cartId 购物车ID
     */
    void removeCartByCartId(Long userId, Long cartId);

    /**
     * 清空用户在指定商家的购物车
     *
     * @param userId 用户ID
     * @param merchantId 商家ID
     */
    void removeCartByMerchantId(Long userId, Long merchantId);

    /**
     * 计算购物车总金额
     *
     * @param items@return 总金额
     */
    BigDecimal calculateTotalAmount(List<CartItemVO> items);

}
