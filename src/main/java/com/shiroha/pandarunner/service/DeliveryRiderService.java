package com.shiroha.pandarunner.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.shiroha.pandarunner.domain.dto.DeliveryRiderDTO;
import com.shiroha.pandarunner.domain.entity.DeliveryRider;
import com.shiroha.pandarunner.domain.vo.AvailableOrderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryRiderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryOrderVO;

/**
 * 骑手表 服务层。
 *
 * @author haowei703
 * @since 2025-07-03
 */
public interface DeliveryRiderService extends IService<DeliveryRider> {

    /**
     * 用户注册为骑手
     *
     * @param userId 用户ID
     * @param deliveryRiderDTO 骑手信息
     * @return 骑手ID
     */
    Long registerRider(Long userId, DeliveryRiderDTO deliveryRiderDTO);

    /**
     * 检查用户是否已注册骑手
     *
     * @param userId 用户ID
     * @return 骑手信息，未注册返回null
     */
    DeliveryRider getRiderByUserId(Long userId);

    /**
     * 获取骑手信息
     *
     * @param userId 用户ID
     * @return 骑手信息
     */
    DeliveryRiderVO getRiderInfo(Long userId);

    /**
     * 更新骑手状态
     *
     * @param userId 用户ID
     * @param status 状态
     */
    void updateRiderStatus(Long userId, DeliveryRider.DeliveryRiderStatus status);

    /**
     * 更新骑手位置
     *
     * @param userId 用户ID
     * @param location 位置信息
     */
    void updateRiderLocation(Long userId, String location);

    /**
     * 分页查询可接订单
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 可接订单列表
     */
    Page<AvailableOrderVO> getAvailableOrders(Long userId, int pageNumber, int pageSize);

    /**
     * 骑手抢单
     *
     * @param userId 骑手用户ID
     * @param orderId 订单ID
     * @return 是否抢单成功
     */
    boolean grabOrder(Long userId, Long orderId);

    /**
     * 开始配送
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 是否开始成功
     */
    boolean startDelivery(Long userId, Long orderId);

    /**
     * 模拟送达
     *
     * @param userId 骑手用户ID
     * @param orderId 订单ID
     * @return 是否送达成功
     */
    boolean completeDelivery(Long userId, Long orderId);

    /**
     * 查询骑手待配送的订单列表
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 骑手订单列表
     */
    Page<DeliveryOrderVO> getPendingDeliveryOrders(Long userId, int pageNumber, int pageSize);

    /**
     * 查询配送中订单
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 骑手配送中的订单列表
     */
    Page<DeliveryOrderVO> getDeliveringOrders(Long userId, int pageNumber, int pageSize);

    /**
     * 查询骑手配送完成的订单列表
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 骑手完成的订单列表
     */
    Page<DeliveryOrderVO> getCompletedOrders(Long userId, int pageNumber, int pageSize);
}
