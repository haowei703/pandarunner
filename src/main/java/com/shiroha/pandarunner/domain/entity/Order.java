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
import java.sql.Timestamp;

/**
 * 订单表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 订单编号，业务唯一标识(如ORD20230701123456)
     */
    private String orderNo;

    /**
     * 关联用户ID，引用t_user表的id
     */
    private Long userId;

    /**
     * 关联商家ID，引用t_merchant表的id
     */
    private Long merchantId;

    /**
     * 配送地址ID
     */
    private Long addressId;

    /**
     * 订单原总金额(商品总额+配送费)
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额(扣除优惠后)
     */
    private BigDecimal actualAmount;

    /**
     * 配送费用
     */
    private BigDecimal deliveryFee;

    /**
     * 打包费用
     */
    private BigDecimal packingFee;

    /**
     * 优惠减免金额(促销/优惠券等)
     */
    private BigDecimal discountAmount;

    /**
     * 用户备注(如"不要辣，放门口")
     */
    private String remark;

    /**
     * 餐具数量(-1-不需要 0-商家按出餐量提供 1-15-用户选择具体数量，不超过15份)
     */
    private Integer utensilsCount;

    /**
     * 订单状态(0-待支付 1-已支付待接单 2-已接单制作中 3-配送中 4-已完成 5-已取消 6-已退款)
     */
    private OrderStatus status;

    /**
     * 支付成功时间
     */
    private Timestamp paymentTime;

    /**
     * 开始配送时间
     */
    private Timestamp deliveryTime;

    /**
     * 订单完成/确认收货时间
     */
    private Timestamp completionTime;

    @Getter
    public enum OrderStatus {
        /**
         * 待支付（订单创建初始状态）
         */
        PENDING_PAYMENT(0, "待支付"),

        /**
         * 已支付待接单（用户已付款）
         */
        PAID(1, "已支付待接单"),

        /**
         * 已接单制作中（商家确认订单）
         */
        ACCEPTED(2, "已接单制作中"),

        /**
         * 配送中（骑手取货后）
         */
        DELIVERING(3, "配送中"),

        /**
         * 已完成（订单正常结束）
         */
        COMPLETED(4, "已完成"),

        /**
         * 已取消（订单终止）
         */
        CANCELLED(5, "已取消"),

        /**
         * 已退款（售后完成）
         */
        REFUNDED(6, "已退款");

        @EnumValue
        @JsonValue
        private final int code;

        private final String description;

        OrderStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static OrderStatus fromCode(int code) {
            for (OrderStatus status : OrderStatus.values()) {
                if (status.code == code) {
                    return status;
                }
            }
            return null;
        }

    }

}
