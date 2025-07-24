package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;
import org.apache.ibatis.type.ArrayTypeHandler;

import java.io.Serial;
import java.io.Serializable;

/**
 * 购物车项表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_cart_item")
@EqualsAndHashCode(callSuper = true)
public class CartItem extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的购物车ID
     */
    private Long cartId;

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 关联的规格选项ID
     */
    @Column(typeHandler = ArrayTypeHandler.class)
    private Long[] optionIds;

    /**
     * 商品数量
     */
    private Integer quantity;

}
