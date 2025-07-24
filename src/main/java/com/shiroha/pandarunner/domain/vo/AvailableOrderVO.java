package com.shiroha.pandarunner.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 可接订单 VO
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
public class AvailableOrderVO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 收货地址ID
     */
    private Long addressId;

    @JsonIgnore
    private String merchantAddressProvince;

    @JsonIgnore
    private String merchantAddressCity;

    @JsonIgnore
    private String merchantAddressDistrict;

    @JsonIgnore
    private String merchantAddressDetailAddress;

    /**
     * 商家地址
     */
    private String merchantAddress;

    @JsonIgnore
    private String userAddressProvince;

    @JsonIgnore
    private String userAddressCity;

    @JsonIgnore
    private String userAddressDistrict;

    @JsonIgnore
    private String userAddressDetailAddress;

    /**
     * 收货地址
     */
    private String deliveryAddress;

    /**
     * 配送费
     */
    private BigDecimal deliveryFee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 预计距离(km)
     */
    private BigDecimal estimatedDistance;

    /**
     * 下单时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

}