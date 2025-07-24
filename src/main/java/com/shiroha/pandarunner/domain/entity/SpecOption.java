package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;

import java.io.Serial;

import lombok.*;

/**
 * 规格选项表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_spec_option")
@EqualsAndHashCode(callSuper = true)
public class SpecOption extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的规格组ID
     */
    private Long groupId;

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 规格选项名称
     */
    private String name;

    /**
     * 价格偏移量
     */
    private BigDecimal priceAdjust;

    /**
     * 是否默认选择
     */
    private Boolean isDefault;

}
