package com.shiroha.pandarunner.service;

import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.entity.OrderItem;
import com.shiroha.pandarunner.domain.vo.OrderItemVO;

import java.util.List;
import java.util.Map;

/**
 * 订单商品表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID获取订单商品项
     *
     * @param orderId 订单ID
     * @return 订单商品项列表
     */
    List<OrderItemVO> getItems(Long orderId);

    /**
     * 批量保存订单项
     *
     * @param items 订单项列表
     */
    void saveItems(List<OrderItem> items);

    /**
     * 批量查询
     *
     * @param orderIds 订单ID列表
     * @return 订单ID-订单商品项列表的映射
     */
    Map<String, List<OrderItemVO>> getItemsByOrderIds(List<Long> orderIds);
}
