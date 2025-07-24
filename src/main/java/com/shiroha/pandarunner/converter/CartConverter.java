package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.CartItemDTO;
import com.shiroha.pandarunner.domain.entity.Cart;
import com.shiroha.pandarunner.domain.entity.CartItem;
import com.shiroha.pandarunner.domain.vo.CartItemVO;
import com.shiroha.pandarunner.domain.vo.CartVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartConverter {

    CartItem cartItemDtoToCartItem(CartItemDTO cartItemDTO);

    List<CartItem> cartItemDtoListToCartItemList(List<CartItemDTO> cartItemDTOList);

    CartVO cartToCartVO(Cart cart);

    List<CartVO> cartListToCartVOList(List<Cart> carts);

    CartItemVO cartItemToCartItemVO(CartItem cartItem);

    List<CartItemVO> cartItemListToCartItemVOList(List<CartItem> cartItems);

}
