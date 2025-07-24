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
 * 支付记录表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_payment_record")
@EqualsAndHashCode(callSuper = true)
public class PaymentRecord extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 支付记录主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 支付流水号
     */
    private String paymentNo;

    /**
     * 支付方式(1-支付宝 2-微信 3-银行卡)
     */
    private PaymentMethod paymentMethod;

    /**
     * 实际支付金额
     */
    private BigDecimal amount;

    /**
     * 支付状态(0-未支付 1-支付成功 2-支付失败 3-已退款)
     */
    private PaymentRecordStatus status;

    /**
     * 支付成功时间
     */
    private Timestamp payTime;

    @Getter
    public enum PaymentMethod {
        /**
         * 支付宝
         */
        ALIPAY(1, "支付宝"),

        /**
         * 微信支付
         */
        WECHAT(2, "微信支付"),

        /**
         * 银行卡
         */
        BANK_CARD(3, "银行卡");

        @EnumValue
        @JsonValue
        private final int code;

        private final String description;

        PaymentMethod(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static PaymentMethod fromCode(int code) {
            for (PaymentMethod method : PaymentMethod.values()) {
                if (method.code == code) {
                    return method;
                }
            }
            return null;
        }
    }

    @Getter
    public enum PaymentRecordStatus {
        /**
         * 未支付（初始状态）
         */
        UNPAID(0, "未支付"),

        /**
         * 支付成功（用户完成支付）
         */
        SUCCESS(1, "支付成功"),

        /**
         * 支付失败（支付流程异常）
         */
        FAILED(2, "支付失败"),

        /**
         * 已退款（售后完成）
         */
        REFUNDED(3, "已退款");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        PaymentRecordStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
