package com.shiroha.pandarunner.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_product")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的商家ID
     */
    private Long merchantId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 是否优先展示
     */
    private Boolean isPriority = false;

    /**
     * 商品当前价格
     */
    private BigDecimal price;

    /**
     * 商品原始价格
     */
    private BigDecimal originalPrice;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 库存(-1表示无限库存)
     */
    private Integer stock;

    /**
     * 商品折扣
     */
    private Integer sales;

    /**
     * 状态(0-下架 1-上架)
     */
    private ProductStatus status = ProductStatus.ONLINE;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    @Getter
    public enum ProductStatus {
        /**
         * 下架（商品不可见）
         */
        OFFLINE(0, "下架"),

        /**
         * 上架（商品可销售）
         */
        ONLINE(1, "上架");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        ProductStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
