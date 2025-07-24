package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 购物车表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_cart")
@EqualsAndHashCode(callSuper = true)
public class Cart extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 关联的商家ID
     */
    private Long merchantId;

}
