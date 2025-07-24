package com.shiroha.pandarunner.controller;

import com.mybatisflex.core.paginate.Page;
import com.shiroha.pandarunner.annotation.SaLoginId;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.config.ValidationGroups;
import com.shiroha.pandarunner.domain.dto.OrderDTO;
import com.shiroha.pandarunner.domain.entity.Order;
import com.shiroha.pandarunner.domain.entity.PaymentRecord;
import com.shiroha.pandarunner.domain.vo.OrderVO;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "订单模块")
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 分页获取订单列表
     *
     * @param pageNumber 页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Operation(summary = "分页获取订单列表")
    @GetMapping("page")
    public R<Page<OrderVO>> page(
            @RequestParam("page_num") int pageNumber,
            @RequestParam("page_size") int pageSize,
            @Min(0)
            @Max(6)
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "merchant_id", required = false) Long merchantId,
            @SaLoginId Long userId) {
        Order.OrderStatus statusEnum = null;
        if (status != null) {
            statusEnum = Order.OrderStatus.fromCode(status);
            if (statusEnum == null) {
                throw new InvalidParamException("订单状态不存在", "status", InvalidParamException.ErrorType.ILLEGAL_VALUE);
            }
        }

        // 根据merchantId是否为空统一调用服务方法
        return R.ok(merchantId == null
                ? orderService.getOrdersByPage(userId, pageNumber, pageSize, statusEnum)
                : orderService.getOrdersByPage(merchantId, false, pageNumber, pageSize, statusEnum)
        );

    }

    /**
     * 用户下单
     *
     * @param dto 下单参数
     * @return 订单详情
     */
    @Operation(summary = "用户下单")
    @PostMapping("save")
    public R<Map<String, Object>> save(@Validated @RequestBody OrderDTO dto, @SaLoginId Long userId) {
        return R.ok(orderService.saveOrder(userId, dto));
    }

    /**
     * 获取订单信息
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    @GetMapping("info/{id}")
    public R<OrderVO> getInfo(@PathVariable("id") Long orderId, @SaLoginId Long userId) {
        return R.ok(orderService.getOrderById(userId, orderId));
    }

    /**
     * 删除订单
     *
     * @param orderId 订单ID
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "删除订单")
    public R<?> remove(@PathVariable("id") Long orderId, @SaLoginId Long userId) {
        orderService.deleteOrder(userId, orderId);
        return R.ok();
    }

    /**
     * 更新订单
     *
     * @param orderDTO 订单信息
     */
    @Operation(summary = "更新订单")
    @PutMapping("update/{id}")
    public R<?> update(@Validated(ValidationGroups.Update.class) @RequestBody OrderDTO orderDTO,
                       @PathVariable("id") Long orderId,
                       @SaLoginId Long userId) {
        orderService.updateOrder(userId, orderId, orderDTO);
        return R.ok();
    }

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @param paymentMethodCode 支付方式代码(1-支付宝 2-微信 3-银行卡)
     * @return 支付流水号
     */
    @Operation(summary = "支付订单")
    @PostMapping("pay/{id}")
    public R<?> payOrder(@PathVariable("id") Long orderId,
                             @RequestParam("payment_method") Integer paymentMethodCode,
                             @SaLoginId Long userId) {
        PaymentRecord.PaymentMethod paymentMethod = PaymentRecord.PaymentMethod.fromCode(paymentMethodCode);
        if (paymentMethod == null) {
            throw new InvalidParamException("支付方式不存在", "payment_method", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }
        String paymentNo = orderService.payOrder(userId, orderId, paymentMethod);
        return R.ok("success").setData(paymentNo);
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     */
    @Operation(summary = "取消订单")
    @PutMapping("cancel/{id}")
    public R<?> cancelOrder(@PathVariable("id") Long orderId, @SaLoginId Long userId) {
        orderService.cancelOrder(userId, orderId);
        return R.ok();
    }
}
