package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.CartConverter;
import com.shiroha.pandarunner.domain.entity.Cart;
import com.shiroha.pandarunner.domain.entity.Merchant;
import com.shiroha.pandarunner.domain.vo.CartItemVO;
import com.shiroha.pandarunner.domain.vo.CartVO;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.mapper.CartMapper;
import com.shiroha.pandarunner.service.CartItemService;
import com.shiroha.pandarunner.service.CartService;
import com.shiroha.pandarunner.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.shiroha.pandarunner.domain.entity.table.CartTableDef.CART;

/**
 * 购物车表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>  implements CartService {

    private final CartConverter cartConverter;
    private final CartItemService cartItemService;
    private final MerchantService merchantService;


    /**
     * 新增用户购物车
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     * @return 购物车ID
     */
    @Override
    public Long saveCart(Long userId, Long merchantId) {
        merchantService.getMerchantById(merchantId, false);

        // 首先检验购物车是否已存在，这里需要筛选逻辑删除字段
        Cart cart;
        cart = getOne(query().where(CART.USER_ID.eq(userId)
                .and(CART.MERCHANT_ID.eq(merchantId)))
                .and(CART.IS_DELETED.in(true, false)));

        if(cart != null) {
            // 逻辑删除字段可能为true，需要再次更新为false
            cart.setIsDeleted(false);
            updateById(cart);
            return cart.getId();
        }

        cart = new Cart();
        cart.setUserId(userId);
        cart.setMerchantId(merchantId);
        save(cart);
        return cart.getId();
    }

    /**
     * 获取用户在指定商家的购物车（含商品项）
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     * @return 购物车信息
     */
    @Override
    public CartVO getCartByMerchantId(Long userId, Long merchantId) {
        Cart cart = getOne(query().where(CART.USER_ID.eq(userId)
                .and(CART.MERCHANT_ID.eq(merchantId))));
        if(cart == null){
            return null;
        }

        CartVO cartVO = cartConverter.cartToCartVO(cart);
        Merchant merchant = merchantService.getMerchantById(cart.getMerchantId());
        cartVO.setMerchantName(merchant.getName());
        cartVO.setMerchantLogo(merchant.getLogo());
        List<CartItemVO> items = cartItemService.getItemsByCartId(cart.getId());
        cartVO.setItems(items);
        cartVO.setTotalPrice(calculateTotalAmount(items));

        return cartVO;
    }

    /**
     * 获取用户全部购物车内容
     *
     * @param userId 用户ID
     * @return 购物车信息列表
     */
    @Override
    public List<CartVO> getCartByUserId(Long userId) {
        List<Cart> carts = list(query().where(CART.USER_ID.eq(userId)));
        if(carts == null || carts.isEmpty()){
            throw new BusinessDataNotFoundException("用户购物车不存在", userId);
        }

        // 收集购物车ID
        List<Long> cartIds = carts.stream().map(Cart::getId).toList();
        // 初始化容器
        List<CartVO> cartVOs = new ArrayList<>(carts.size());

        Map<String, List<CartItemVO>> itemsByMerchantId = cartItemService.getItemsByCartIdsWithMerchantId(cartIds);
        if(itemsByMerchantId.isEmpty()) {
            return cartVOs;
        }
        List<Long> merchantIds = itemsByMerchantId.keySet().stream().map(Long::valueOf).toList();
        List<Merchant> merchants = merchantService.getMerchantsByIds(merchantIds);
        Map<Long, Merchant> merchantMap = merchants.stream().collect(Collectors.toMap(Merchant::getId, Function.identity()));

        for(Cart cart : carts){
            CartVO cartVO = cartConverter.cartToCartVO(cart);
            Merchant merchant = merchantMap.get(cart.getMerchantId());
            // 如果获取到购物车项不包含该商家的商品则跳过
            if(merchant == null) {
                continue;
            }

            cartVO.setMerchantName(merchant.getName());
            cartVO.setMerchantLogo(merchant.getLogo());
            List<CartItemVO> items = itemsByMerchantId.get(String.valueOf(cart.getMerchantId()));
            cartVO.setItems(items);
            cartVO.setTotalPrice(calculateTotalAmount(items));
            cartVOs.add(cartVO);
        }

        return cartVOs;
    }

    /**
     * 清空指定购物车中的全部商品
     *
     * @param userId 用户ID
     * @param cartId 购物车ID
     */
    @Override
    public void removeCartByCartId(Long userId, Long cartId) {
        boolean isCartBelongToUser = exists(query()
                .where(CART.ID.eq(cartId))
                .and(CART.USER_ID.eq(userId)));
        if(!isCartBelongToUser) {
            throw new NotSupportException("不允许的操作");
        }
        cartItemService.removeItemsByCartId(cartId);
    }

    /**
     * 清空用户在指定商家的购物车
     *
     * @param userId     用户ID
     * @param merchantId 商家ID
     */
    @Override
    public void removeCartByMerchantId(Long userId, Long merchantId) {
        // 获取到购物车ID
        Long cartId = getOneAs(query()
                .select(CART.ID)
                .where(CART.USER_ID.eq(userId))
                .and(CART.MERCHANT_ID.eq(merchantId)), Long.class);
        // 删除购物车中全部商品
        cartItemService.removeItemsByCartId(cartId);
    }

    /**
     * 计算购物车总金额
     *
     * @param items@return 总金额
     */
    @Override
    public BigDecimal calculateTotalAmount(List<CartItemVO> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItemVO item : items) {
            totalAmount = totalAmount.add(item.getPrice());
        }

        return totalAmount;
    }
}
