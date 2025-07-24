package com.shiroha.pandarunner.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;

import java.io.Serial;

import lombok.*;

/**
 * 订单商品表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_order_item")
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单商品项主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的订单ID，引用t_order表的id
     */
    private Long orderId;

    /**
     * 关联的商品ID，引用t_product表的id
     */
    private Long productId;

    /**
     * 商品名称快照（下单时的名称，避免商品更名影响历史订单）
     */
    private String productName;

    /**
     * 商品图片快照URL（下单时的图片）
     */
    private String productImage;

    /**
     * 商品单价快照（下单时的价格，单位：元）
     */
    private BigDecimal price;

    /**
     * 购买数量（正整数）
     */
    private Integer quantity;

    /**
     * 规格组合信息，格式为"规格1,规格2,..."的字符串（如"大杯,少冰,半糖"）
     */
    private String spec;

    /**
     * 该商品项总金额（计算公式：price × quantity）
     */
    private BigDecimal totalPrice;
}
