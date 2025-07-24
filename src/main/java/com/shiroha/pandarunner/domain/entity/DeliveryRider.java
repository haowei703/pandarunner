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
 * 骑手表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_delivery_rider")
@EqualsAndHashCode(callSuper = true)
public class DeliveryRider extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 骑手主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 骑手姓名
     */
    private String name;

    /**
     * 骑手电话
     */
    private String phone;

    /**
     * 骑手身份证号
     */
    private String idCard;

    /**
     * 状态(0-休息 1-可接单 2-忙碌)
     */
    private DeliveryRiderStatus status;

    /**
     * 骑手位置
     */
    private String currentLocation;

    /**
     * 骑手评分(1.0-5.0)
     */
    private BigDecimal rating;

    /**
     * 累计配送单数
     */
    private Integer deliveryCount;

    @Getter
    public enum DeliveryRiderStatus {
        /**
         * 休息（不可接单）
         */
        RESTING(0, "休息"),

        /**
         * 可接单（在线且空闲）
         */
        AVAILABLE(1, "可接单"),

        /**
         * 忙碌（正在配送中）
         */
        BUSY(2, "忙碌");

        @EnumValue
        @JsonValue
        private final int code;

        private final String description;

        DeliveryRiderStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

}
