package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品分类表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_product_category")
@EqualsAndHashCode(callSuper = true)
public class ProductCategory extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的商家ID
     */
    private Long merchantId;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 排序权重
     */
    private Integer sortOrder;
}
