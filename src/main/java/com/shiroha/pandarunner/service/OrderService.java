package com.shiroha.pandarunner.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.OrderDTO;
import com.shiroha.pandarunner.domain.entity.Order;
import com.shiroha.pandarunner.domain.entity.PaymentRecord;
import com.shiroha.pandarunner.domain.vo.OrderVO;

import java.util.Map;

/**
 * 订单表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据ID查询订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return vo对象
     */
    OrderVO getOrderById(Long userId, Long orderId);

    /**
     * 新增订单
     *
     * @param userId   下单的用户ID
     * @param orderDTO 订单DTO
     * @return 订单ID
     */
    Map<String, Object> saveOrder(Long userId, OrderDTO orderDTO);

    /**
     * 更新订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @param orderDTO dto对象
     */
    void updateOrder(Long userId, Long orderId, OrderDTO orderDTO);

    /**
     * 删除订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void deleteOrder(Long userId, Long orderId);

    /**
     * 分页获取订单
     *
     * @param inquirerId 操作者ID
     * @param isUser 操作者是否是用户
     * @param pageNumber 当前页数
     * @param pageSize 页面大小
     * @param status 订单状态
     * @return 分页对象
     */
    Page<OrderVO> getOrdersByPage(Long inquirerId, boolean isUser, int pageNumber, int pageSize, Order.OrderStatus status);

    /**
     * 重载方法，返回部分状态订单
     *
     * @param userId 用户ID
     * @param pageNumber 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
    default Page<OrderVO> getOrdersByPage(Long userId, int pageNumber, int pageSize, Order.OrderStatus status) {
        return getOrdersByPage(userId, true, pageNumber, pageSize, status);
    }

    /**
     * 重载方法，返回全部订单
     *
     * @param userId 用户ID
     * @param pageNumber 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
    default Page<OrderVO> getOrdersByPage(Long userId, int pageNumber, int pageSize) {
        return getOrdersByPage(userId, true, pageNumber, pageSize, null);
    }

    /**
     * 检验订单属于用户
     *
     * @param order     订单
     * @param userId    用户ID
     */
    void validateOrderBelongToUser(Order order, Long userId);

    /**
     * 支付订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentMethod 支付方式
     * @return 支付流水号
     */
    String payOrder(Long userId, Long orderId, PaymentRecord.PaymentMethod paymentMethod);

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void cancelOrder(Long userId, Long orderId);
}
