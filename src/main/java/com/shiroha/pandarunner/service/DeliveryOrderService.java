package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.entity.DeliveryOrder;

/**
 * 配送订单表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface DeliveryOrderService extends IService<DeliveryOrder> {

    /**
     * 根据订单ID和骑手ID查询配送订单
     *
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 配送订单
     */
    DeliveryOrder getByOrderIdAndRiderId(Long orderId, Long riderId);

    /**
     * 开始配送订单
     *
     * @param deliveryOrder 配送订单
     * @return 是否成功
     */
    boolean startDelivery(DeliveryOrder deliveryOrder);

    /**
     * 完成配送订单
     *
     * @param deliveryOrder 配送订单
     * @return 是否成功
     */
    boolean completeDelivery(DeliveryOrder deliveryOrder);
}
