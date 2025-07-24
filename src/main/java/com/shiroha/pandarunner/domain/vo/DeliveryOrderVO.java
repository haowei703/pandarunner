package com.shiroha.pandarunner.domain.vo;

import com.shiroha.pandarunner.domain.entity.DeliveryOrder;
import com.shiroha.pandarunner.domain.entity.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 配送订单 VO
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Data
public class DeliveryOrderVO {

    /**
     * 配送订单ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 配送状态
     */
    private DeliveryOrder.DeliveryOrderStatus status;

    /**
     * 接单时间
     */
    private Timestamp acceptTime;

    /**
     * 完成时间
     */
    private Timestamp completionTime;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private Order.OrderStatus orderStatus;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 配送地址
     */
    private String deliveryAddress;
}