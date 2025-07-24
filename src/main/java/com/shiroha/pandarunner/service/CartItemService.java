package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.CartItemDTO;
import com.shiroha.pandarunner.domain.entity.CartItem;
import com.shiroha.pandarunner.domain.vo.CartItemVO;

import java.util.List;
import java.util.Map;

/**
 * 购物车项表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface CartItemService extends IService<CartItem> {

    /**
     * 添加商品到购物车中
     *
     * @param cartId      购物车ID
     * @param cartItemDTO 购物车项
     * @return
     */
    Long saveItem(Long cartId, CartItemDTO cartItemDTO);

    /**
     * 获取购物车中全部的商品
     *
     * @param cartId    购物车ID
     * @return          商品列表
     */
    List<CartItemVO> getItemsByCartId(Long cartId);

    /**
     * 批量获取购物车项
     *
     * @param cartIds 购物车ID列表
     * @return 商家ID-购物车项映射
     */
    Map<String, List<CartItemVO>> getItemsByCartIdsWithMerchantId(List<Long> cartIds);

    /**
     * 移除购物车商品
     *
     * @param itemId 购物车项id
     */
    void removeItemById(Long itemId);

    /**
     * 清空购物车下的全部商品
     *
     * @param cartId 购物车ID
     */
    void removeItemsByCartId(Long cartId);

    /**
     * 更新购物车中商品数量
     *
     * @param userId   用户ID
     * @param itemId   购物车项ID
     * @param quantity 更新后的数量
     */
    void updateItemsQuantityById(Long userId, Long itemId, Integer quantity);

    /**
     * 批量删除购物车下的商品
     *
     * @param itemIds 购物车ID列表
     */
    void removeItemsByItemIds(List<Long> itemIds);

}
