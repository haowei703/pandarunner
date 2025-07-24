package com.shiroha.pandarunner.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.shiroha.pandarunner.domain.entity.CartItem}
 */
@Data
public class CartItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项ID
     */
    private Long id;

    /**
     * 商品信息
     */
    private ProductVO product;

    /**
     * 规格选项列表
     */
    private Long[] optionIds;

    /**
     * 规格选项名称
     */
    private String spec;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 购物车单项总价
     */
    private BigDecimal price;

}
