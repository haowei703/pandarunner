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
 * 商家信息表 实体类。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_merchant")
@EqualsAndHashCode(callSuper = true)
public class Merchant extends BaseEntity implements Address, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家描述
     */
    private String description;

    /**
     * 商家Logo图片URL
     */
    private String logo;

    /**
     * 商家联系电话
     */
    private String contactPhone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String district;

    /**
     * 商家详细地址
     */
    private String detailAddress;

    /**
     * 商家实体店经纬度
     */
    private String location;

    /**
     * 营业执照编号
     */
    private String businessLicense;

    /**
     * 状态(0-待审核 1-正常 2-禁用)
     */
    private MerchantStatus status = MerchantStatus.PENDING_REVIEW;

    /**
     * 平均配送时间(分钟)
     */
    private Integer avgDeliveryTime;

    /**
     * 最低起送金额
     */
    private BigDecimal minOrderAmount;

    /**
     * 配送费
     */
    private BigDecimal deliveryFee;

    /**
     * 打包费
     */
    private BigDecimal packingFee;

    /**
     * 商家评分(1.0-5.0)
     */
    private BigDecimal score;

    /**
     * 月销量
     */
    private Integer monthlySales;

    /**
     * 历史总销量
     */
    private Integer totalSales;

    @Getter
    public enum MerchantStatus {
        /**
         * 待审核
         */
        PENDING_REVIEW(0, "待审核"),

        /**
         * 正常
         */
        NORMAL(1, "正常"),

        /**
         * 禁用
         */
        DISABLED(2, "禁用");

        @EnumValue
        private final int code;

        @JsonValue
        private final String description;

        MerchantStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

    }
}
