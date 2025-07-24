package com.shiroha.pandarunner.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.CartItem}
 */
@Data
public class CartItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 关联的规格选项ID
     */
    private Long[] optionIds;

    /**
     * 商品数量
     */
    private Integer quantity;
}
