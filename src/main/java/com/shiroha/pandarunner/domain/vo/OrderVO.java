package com.shiroha.pandarunner.domain.vo;

import com.shiroha.pandarunner.domain.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 订单详情VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单详情VO")
public class OrderVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "商家ID")
    private Long merchantId;

    @Schema(description = "收货地址ID")
    private Long addressId;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal actualAmount;

    @Schema(description = "配送费")
    private BigDecimal deliveryFee;

    @Schema(description = "打包费用")
    private BigDecimal packingFee;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "餐具数量")
    private Integer utensilsCount;

    @Schema(description = "订单状态")
    private Order.OrderStatus status;

    @Schema(description = "支付时间")
    private Timestamp paymentTime;

    @Schema(description = "配送时间")
    private Timestamp deliveryTime;

    @Schema(description = "完成时间")
    private Timestamp completionTime;

    @Schema(description = "创建时间")
    private Timestamp createdAt;

    @Schema(description = "订单项列表")
    private List<OrderItemVO> items;
}