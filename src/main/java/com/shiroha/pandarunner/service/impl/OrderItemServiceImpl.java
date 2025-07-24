package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.OrderConverter;
import com.shiroha.pandarunner.domain.entity.OrderItem;
import com.shiroha.pandarunner.domain.vo.OrderItemVO;
import com.shiroha.pandarunner.mapper.OrderItemMapper;
import com.shiroha.pandarunner.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shiroha.pandarunner.domain.entity.table.OrderItemTableDef.ORDER_ITEM;

/**
 * 订单商品表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "orderItem")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    private final OrderConverter converter;

    /**
     * 根据订单ID获取订单商品项
     *
     * @param orderId 订单ID
     * @return 订单商品项列表
     */
    @Override
    @Cacheable(key = "#orderId")
    public List<OrderItemVO> getItems(Long orderId) {
        List<OrderItem> items = list(query().where(ORDER_ITEM.ORDER_ID.eq(orderId)));
        return converter.orderItemListToOrderItemVOList(items);
    }

    /**
     * 批量保存订单项
     *
     * @param items 订单项列表
     */
    @Override
    public void saveItems(List<OrderItem> items) {
        saveBatch(items);
    }

    /**
     * 批量查询
     *
     * @param orderIds 订单ID列表
     * @return 订单ID-订单商品项列表的映射
     */
    @Override
    @Cacheable(
            cacheNames = "itemsByOrderIds",
            keyGenerator = "ArrayKeyGenerator"
    )
    public Map<String, List<OrderItemVO>> getItemsByOrderIds(List<Long> orderIds) {
        // 初始化map，预填充key和空列表
        Map<String, List<OrderItemVO>> itemsByOrderId = new HashMap<>((int) (orderIds.size() / 0.75f));
        for (Long orderId : orderIds) {
            itemsByOrderId.put(String.valueOf(orderId), new ArrayList<>());
        }

        // 遍历批量查询结果，将item插入到对应列表
        list(query().where(ORDER_ITEM.ORDER_ID.in(orderIds))).forEach(item -> {
            List<OrderItemVO> items = itemsByOrderId.get(String.valueOf(item.getOrderId()));
            items.add(converter.orderItemToOrderItemVO(item));
        });

        return itemsByOrderId;
    }
}
