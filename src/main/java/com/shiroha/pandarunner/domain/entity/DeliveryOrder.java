package com.shiroha.pandarunner.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 配送订单表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_delivery_order")
@EqualsAndHashCode(callSuper = true)
public class DeliveryOrder extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配送订单主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 骑手ID
     */
    private Long deliveryRiderId;

    /**
     * 接单时间
     */
    private Timestamp acceptTime;

    /**
     * 取货时间
     */
    private Timestamp pickupTime;

    /**
     * 开始配送时间
     */
    private Timestamp deliverTime;

    /**
     * 配送完成时间
     */
    private Timestamp completionTime;

    /**
     * 配送状态(0-待接单 1-已接单 2-已取货 3-配送中 4-已完成 5-已取消)
     */
    private DeliveryOrderStatus status;

    @Getter
    public enum DeliveryOrderStatus {
        /**
         * 待接单
         */
        PENDING(0, "待接单"),

        /**
         * 已接单
         */
        ACCEPTED(1, "已接单"),

        /**
         * 已取货
         */
        PICKED_UP(2, "已取货"),

        /**
         * 配送中
         */
        DELIVERING(3, "配送中"),

        /**
         * 已完成
         */
        COMPLETED(4, "已完成"),

        /**
         * 已取消
         */
        CANCELLED(5, "已取消");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        DeliveryOrderStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
