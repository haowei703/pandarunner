package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.entity.PaymentRecord;

/**
 * 支付记录表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface PaymentRecordService extends IService<PaymentRecord> {

    /**
     * 创建支付记录
     *
     * @param orderId 订单ID
     * @param paymentMethod 支付方式
     * @return 支付记录ID
     */
    Long createPaymentRecord(Long orderId, PaymentRecord.PaymentMethod paymentMethod);

    /**
     * 模拟支付成功
     *
     * @param paymentNo 支付流水号
     * @return 是否成功
     */
    boolean simulatePaymentSuccess(String paymentNo);

    /**
     * 根据订单ID获取支付记录
     *
     * @param orderId 订单ID
     * @return 支付记录
     */
    PaymentRecord getByOrderId(Long orderId);
}
