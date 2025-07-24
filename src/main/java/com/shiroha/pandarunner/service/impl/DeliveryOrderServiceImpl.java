package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.domain.entity.DeliveryOrder;
import com.shiroha.pandarunner.mapper.DeliveryOrderMapper;
import com.shiroha.pandarunner.service.DeliveryOrderService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.shiroha.pandarunner.domain.entity.table.DeliveryOrderTableDef.DELIVERY_ORDER;

/**
 * 配送订单表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
public class DeliveryOrderServiceImpl extends ServiceImpl<DeliveryOrderMapper, DeliveryOrder> implements DeliveryOrderService {

    /**
     * 根据订单ID和骑手ID查询配送订单
     *
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 配送订单
     */
    @Override
    public DeliveryOrder getByOrderIdAndRiderId(Long orderId, Long riderId) {
        return getOne(query()
                .where(DELIVERY_ORDER.ORDER_ID.eq(orderId))
                .and(DELIVERY_ORDER.DELIVERY_RIDER_ID.eq(riderId)));
    }

    /**
     * 开始配送订单
     *
     * @param deliveryOrder 配送订单
     * @return 是否成功
     */
    @Override
    public boolean startDelivery(DeliveryOrder deliveryOrder) {
        deliveryOrder.setStatus(DeliveryOrder.DeliveryOrderStatus.DELIVERING);
        deliveryOrder.setPickupTime(Timestamp.valueOf(LocalDateTime.now()));
        return updateById(deliveryOrder);
    }

    /**
     * 完成配送订单
     *
     * @param deliveryOrder 配送订单
     * @return 是否成功
     */
    @Override
    public boolean completeDelivery(DeliveryOrder deliveryOrder) {
        deliveryOrder.setStatus(DeliveryOrder.DeliveryOrderStatus.COMPLETED);
        deliveryOrder.setCompletionTime(Timestamp.valueOf(LocalDateTime.now()));
        return updateById(deliveryOrder);
    }
}
