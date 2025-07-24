package com.shiroha.pandarunner.domain.vo;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的商家ID
     */
    private Long merchantId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 商家LOGO
     */
    private String merchantLogo;

    /**
     * 购物车项列表
     */
    private List<CartItemVO> items;

    /**
     * 购物车商品总价
     */
    private BigDecimal totalPrice;

}
