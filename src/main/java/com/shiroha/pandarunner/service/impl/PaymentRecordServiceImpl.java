package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.domain.entity.Order;
import com.shiroha.pandarunner.domain.entity.PaymentRecord;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.mapper.PaymentRecordMapper;
import com.shiroha.pandarunner.service.OrderService;
import com.shiroha.pandarunner.service.PaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.shiroha.pandarunner.domain.entity.table.PaymentRecordTableDef.PAYMENT_RECORD;

/**
 * 支付记录表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {

    private final OrderService orderService;

    @Autowired
    public PaymentRecordServiceImpl(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建支付记录
     *
     * @param orderId 订单ID
     * @param paymentMethod 支付方式
     * @return 支付记录ID
     */
    @Override
    @Transactional
    public Long createPaymentRecord(Long orderId, PaymentRecord.PaymentMethod paymentMethod) {
        // 验证订单存在且状态为待支付
        Order order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessDataNotFoundException("订单不存在", orderId);
        }
        
        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new InvalidParamException("订单状态不允许支付", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 检查是否已存在支付记录
        PaymentRecord existingRecord = getByOrderId(orderId);
        if (existingRecord != null) {
            throw new InvalidParamException("订单已存在支付记录", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 生成支付流水号
        String paymentNo = generatePaymentNo(orderId);

        PaymentRecord paymentRecord = PaymentRecord.builder()
                .orderId(orderId)
                .paymentNo(paymentNo)
                .paymentMethod(paymentMethod)
                .amount(order.getActualAmount())
                .status(PaymentRecord.PaymentRecordStatus.UNPAID)
                .build();

        save(paymentRecord);
        return paymentRecord.getId();
    }

    /**
     * 模拟支付成功
     *
     * @param paymentNo 支付流水号
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean simulatePaymentSuccess(String paymentNo) {
        PaymentRecord paymentRecord = QueryChain.of(mapper)
                .where(PAYMENT_RECORD.PAYMENT_NO.eq(paymentNo))
                .one();

        if (paymentRecord == null) {
            throw new BusinessDataNotFoundException("支付记录不存在", paymentNo);
        }

        if (paymentRecord.getStatus() != PaymentRecord.PaymentRecordStatus.UNPAID) {
            throw new InvalidParamException("支付记录状态不允许支付", "paymentNo", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 更新支付记录状态
        paymentRecord.setStatus(PaymentRecord.PaymentRecordStatus.SUCCESS);
        paymentRecord.setPayTime(Timestamp.valueOf(LocalDateTime.now()));
        updateById(paymentRecord);

        // 更新订单状态
        Order order = orderService.getById(paymentRecord.getOrderId());
        order.setStatus(Order.OrderStatus.PAID);
        order.setPaymentTime(paymentRecord.getPayTime());
        orderService.updateById(order);

        return true;
    }

    /**
     * 根据订单ID获取支付记录
     *
     * @param orderId 订单ID
     * @return 支付记录
     */
    @Override
    public PaymentRecord getByOrderId(Long orderId) {
        return QueryChain.of(mapper)
                .where(PAYMENT_RECORD.ORDER_ID.eq(orderId))
                .one();
    }

    /**
     * 生成支付流水号
     *
     * @param orderId 订单ID
     * @return 支付流水号
     */
    private String generatePaymentNo(Long orderId) {
        return "PAY" + System.currentTimeMillis() + orderId;
    }
}
