package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.CartConverter;
import com.shiroha.pandarunner.domain.dto.CartItemDTO;
import com.shiroha.pandarunner.domain.entity.Cart;
import com.shiroha.pandarunner.domain.entity.CartItem;
import com.shiroha.pandarunner.domain.vo.CartItemVO;
import com.shiroha.pandarunner.domain.vo.ProductVO;
import com.shiroha.pandarunner.domain.vo.SpecOptionVO;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.mapper.CartItemMapper;
import com.shiroha.pandarunner.mapper.CartMapper;
import com.shiroha.pandarunner.mapper.SpecOptionMapper;
import com.shiroha.pandarunner.service.CartItemService;
import com.shiroha.pandarunner.service.ProductService;
import com.shiroha.pandarunner.service.SpecOptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.shiroha.pandarunner.domain.entity.table.CartItemTableDef.CART_ITEM;
import static com.shiroha.pandarunner.domain.entity.table.CartTableDef.CART;
import static com.shiroha.pandarunner.domain.entity.table.SpecOptionTableDef.SPEC_OPTION;

/**
 * 购物车项表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem>  implements CartItemService {

    private final CartConverter cartConverter;
    private final ProductService productService;
    private final SpecOptionService specOptionService;
    private final CartMapper cartMapper;
    private final SpecOptionMapper specOptionMapper;

    /**
     * 添加商品到购物车中
     *
     * @param cartId      购物车ID
     * @param cartItemDTO 购物车项
     * @return 购物车项ID
     */
    @Override
    public Long saveItem(Long cartId, CartItemDTO cartItemDTO) {
        // 检验是否存在购物车
        boolean isCartExisted = QueryChain.of(cartMapper)
                .where(CART.ID.eq(cartId))
                .exists();

        Long[] optionIds = cartItemDTO.getOptionIds();
        if(optionIds != null) {
            // 检验是否存在规格选项
            boolean isOptionExisted = QueryChain.of(specOptionMapper)
                    .where(SPEC_OPTION.PRODUCT_ID.eq(cartItemDTO.getProductId()))
                    .and(SPEC_OPTION.ID.in(Arrays.stream(optionIds).toList()))
                    .exists();
            // 是否允许添加规格
            boolean optionAllowed = (isOptionExisted && optionIds.length > 0)
                    || (!isOptionExisted && optionIds.length == 0);

            if(!isCartExisted || !optionAllowed) {
                throw new NotSupportException("操作失败");
            }
        }

        CartItem cartItem = cartConverter.cartItemDtoToCartItem(cartItemDTO);
        cartItem.setCartId(cartId);
        save(cartItem);
        return cartItem.getId();
    }

    /**
     * 获取购物车中全部的商品
     *
     * @param cartId 购物车ID
     * @return 商品列表
     */
    @Override
    public List<CartItemVO> getItemsByCartId(Long cartId) {
        // 查询购物车中的全部商品
        List<CartItem> items = list(query()
                .where(CART_ITEM.CART_ID.eq(cartId))
                .orderBy(CART_ITEM.ID.desc())); // 按购物车项ID降序排列，确保数据有序性
        if(items.isEmpty()) {
            return List.of();
        }
        // 收集商品ID
        List<Long> productIds = items.stream().map(CartItem::getProductId).toList();
        // 批量查询商品
        List<ProductVO> products = productService.getProductsByIds(productIds);

        // 商品ID映射
        Map<Long, ProductVO> productMap = products.stream()
                .collect(Collectors.toMap(ProductVO::getId, Function.identity()));


        // 收集规格ID
        List<Long> optionIds = items.stream().map(CartItem::getOptionIds)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .toList();
        // 查询规格信息
        List<SpecOptionVO> options = specOptionService.getSpecOptionsByIds(optionIds);

        // 规格ID映射
        Map<Long, SpecOptionVO> specOptionMap = options.stream()
                .collect(Collectors.toMap(SpecOptionVO::getId, Function.identity()));

        // 初始化容器
        List<CartItemVO> itemVOS = new ArrayList<>(items.size());
        // 遍历全部购物车项，填充数据
        for (CartItem cartItem : items) {
            // 转换为VO对象
            CartItemVO cartItemVO = cartConverter.cartItemToCartItemVO(cartItem);
            // 获取商品信息
            ProductVO productVO = productMap.get(cartItem.getProductId());
            cartItemVO.setProduct(productVO);

            // 保存商品价格
            BigDecimal price = productVO.getPrice();

            // 获取规格信息
            Long[] itemOptions = cartItem.getOptionIds();
            // 循环单个商品的规格列表，分别设置vo对象
            if(itemOptions != null && itemOptions.length > 0) {
                Arrays.stream(itemOptions)
                        .filter(Objects::nonNull)
                        .forEach(optionId -> {
                            SpecOptionVO option = specOptionMap.get(optionId);
                            if (option != null && option.getName() != null) {
                                String newSpec = option.getName();
                                cartItemVO.setSpec(Optional.ofNullable(cartItemVO.getSpec())
                                        .filter(s -> !s.isEmpty())
                                        .map(s -> s + "," + newSpec)
                                        .orElse(newSpec));
                            }
                        });

                // 累加每个规格选项的价格偏移
                for(SpecOptionVO option : options) {
                    price = price.add(option.getPriceAdjust());
                }
            }

            // 计算总价 = (商品单价 + 规格价格偏移) * 商品数量
            price = price.multiply(BigDecimal.valueOf(cartItemVO.getQuantity()));

            cartItemVO.setPrice(price);

            itemVOS.add(cartItemVO);
        }

        return itemVOS;
    }

    /**
     * 批量获取购物车项
     *
     * @param cartIds 购物车ID列表
     * @return 购物车ID-购物车项映射
     */
    @Override
    public Map<String, List<CartItemVO>> getItemsByCartIdsWithMerchantId(List<Long> cartIds) {
        // 批量查询购物车项
        List<CartItem> items = list(query()
                .where(CART_ITEM.CART_ID.in(cartIds))
                .orderBy(CART_ITEM.ID.desc()));

        if(items.isEmpty()) {
            return Map.of();
        }

        // 收集商品ID
        List<Long> productIds = items.stream().map(CartItem::getProductId).toList();
        // 批量查询商品
        List<ProductVO> products = productService.getProductsByIds(productIds);

        // 商品ID映射
        Map<Long, ProductVO> productMap = products.stream()
                .collect(Collectors.toMap(ProductVO::getId, Function.identity()));

        // 收集规格ID
        List<Long> optionIds = items.stream().map(CartItem::getOptionIds)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .toList();
        // 查询规格信息
        List<SpecOptionVO> options = specOptionService.getSpecOptionsByIds(optionIds);

        // 规格ID映射
        Map<Long, SpecOptionVO> specOptionMap = options.stream()
                .collect(Collectors.toMap(SpecOptionVO::getId, Function.identity()));

        // 批量查询购物车表，获取购物车ID-商家ID的映射关系
        Map<Long, Long> cartIdToMerchantId = QueryChain.of(cartMapper)
                .select(CART.ID, CART.MERCHANT_ID)
                .where(CART.ID.in(cartIds))
                .list()
                .stream()
                .collect(Collectors.toMap(Cart::getId, Cart::getMerchantId));

        // 按商家ID分组的结果Map
        Map<String, List<CartItemVO>> resultMap = new HashMap<>();

        // 遍历全部购物车项，填充数据
        for (CartItem cartItem : items) {
            // 转换为VO对象
            CartItemVO cartItemVO = cartConverter.cartItemToCartItemVO(cartItem);
            // 获取商品信息
            ProductVO productVO = productMap.get(cartItem.getProductId());
            cartItemVO.setProduct(productVO);

            // 保存商品价格
            BigDecimal price = productVO.getPrice();

            // 获取规格信息
            Long[] itemOptions = cartItem.getOptionIds();
            // 循环单个商品的规格列表，分别设置vo对象
            if(itemOptions != null && itemOptions.length > 0) {
                Arrays.stream(itemOptions)
                        .filter(Objects::nonNull)
                        .forEach(optionId -> {
                            SpecOptionVO option = specOptionMap.get(optionId);
                            if (option != null && option.getName() != null) {
                                String newSpec = option.getName();
                                cartItemVO.setSpec(Optional.ofNullable(cartItemVO.getSpec())
                                        .filter(s -> !s.isEmpty())
                                        .map(s -> s + "," + newSpec)
                                        .orElse(newSpec));
                            }
                        });

                // 计算总价 = (商品单价 + 规格价格偏移) * 商品数量
                // 累加每个规格选项的价格偏移
                for(SpecOptionVO option : options) {
                    price = price.add(option.getPriceAdjust());
                }
            }
            price = price.multiply(BigDecimal.valueOf(cartItemVO.getQuantity()));

            cartItemVO.setPrice(price);

            // 获取商家ID
            Long merchantId = cartIdToMerchantId.get(cartItem.getCartId());
            resultMap.computeIfAbsent(String.valueOf(merchantId), k -> new ArrayList<>()).add(cartItemVO);
        }

        return resultMap;
    }

    /**
     * 移除购物车商品
     *
     * @param itemId 购物车项id
     */
    @Override
    public void removeItemById(Long itemId) {
        if(!removeById(itemId)) {
            throw new BusinessDataNotFoundException("购物项不存在", itemId);
        }
    }

    /**
     * 清空购物车下的全部商品
     *
     * @param cartId 购物车ID
     */
    @Override
    public void removeItemsByCartId(Long cartId) {
        if(!remove(query().where(CART_ITEM.CART_ID.eq(cartId)))) {
            throw new BusinessDataNotFoundException("购物车不存在", cartId);
        }
    }

    /**
     * 更新购物车中商品数量
     *
     * @param userId   用户ID
     * @param itemId   购物车项ID
     * @param quantity 更新后的数量
     */
    @Override
    public void updateItemsQuantityById(Long userId, Long itemId, Integer quantity) {
        // 联合查询，验证购物车项属于用户
        boolean isCartItemBelongToUser = QueryChain.of(cartMapper)
                .from(CART)
                .leftJoin(CART_ITEM).on(CART_ITEM.CART_ID.eq(CART.ID))
                .where(CART.USER_ID.eq(userId))
                .exists();

        if(!isCartItemBelongToUser) {
            throw new NotSupportException("操作失败");
        }

        // 查询购物车项，如果数量大于0
        CartItem item = getOne(query().where(CART_ITEM.ID.eq(itemId)));
        if(item == null) {
            return;
        }

        if (quantity == 0) {
            removeById(itemId);
        } else if (!item.getQuantity().equals(quantity)) {
            item.setQuantity(quantity);
            updateById(item);
        }
    }

    /**
     * 批量删除购物车下的商品
     *
     * @param itemIds 购物车ID列表
     */
    @Override
    public void removeItemsByItemIds(List<Long> itemIds) {
        removeByIds(itemIds);
    }
}
