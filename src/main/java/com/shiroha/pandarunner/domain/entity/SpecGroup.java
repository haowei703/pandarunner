package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import lombok.*;

/**
 * 规格组表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_spec_group")
@EqualsAndHashCode(callSuper = true)
public class SpecGroup extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的商品ID
     */
    private Long productId;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 是否必选
     */
    private Boolean isRequired;

}
