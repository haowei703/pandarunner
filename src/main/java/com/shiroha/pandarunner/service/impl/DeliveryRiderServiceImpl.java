package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.DeliveryRiderConverter;
import com.shiroha.pandarunner.domain.dto.DeliveryRiderDTO;
import com.shiroha.pandarunner.domain.entity.DeliveryOrder;
import com.shiroha.pandarunner.domain.entity.DeliveryRider;
import com.shiroha.pandarunner.domain.entity.Order;
import com.shiroha.pandarunner.domain.vo.AvailableOrderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryOrderVO;
import com.shiroha.pandarunner.domain.vo.DeliveryRiderVO;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.exception.NotSupportException;
import com.shiroha.pandarunner.mapper.DeliveryRiderMapper;
import com.shiroha.pandarunner.service.DeliveryOrderService;
import com.shiroha.pandarunner.service.DeliveryRiderService;
import com.shiroha.pandarunner.service.MerchantService;
import com.shiroha.pandarunner.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.shiroha.pandarunner.domain.entity.table.DeliveryOrderTableDef.DELIVERY_ORDER;
import static com.shiroha.pandarunner.domain.entity.table.DeliveryRiderTableDef.DELIVERY_RIDER;
import static com.shiroha.pandarunner.domain.entity.table.MerchantTableDef.MERCHANT;
import static com.shiroha.pandarunner.domain.entity.table.OrderTableDef.ORDER;
import static com.shiroha.pandarunner.domain.entity.table.UserAddressTableDef.USER_ADDRESS;

/**
 * 骑手表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
public class DeliveryRiderServiceImpl extends ServiceImpl<DeliveryRiderMapper, DeliveryRider> implements DeliveryRiderService {

    private final OrderService orderService;
    private final DeliveryRiderConverter converter;
    private final DeliveryOrderService deliveryOrderService;
    private final MerchantService merchantService;

    /**
     * 用户注册为骑手
     *
     * @param userId 用户ID
     * @param deliveryRiderDTO 骑手信息
     * @return 骑手ID
     */
    @Override
    @Transactional
    public Long registerRider(Long userId, DeliveryRiderDTO deliveryRiderDTO) {
        // 检查用户是否已注册骑手
        DeliveryRider existingRider = getRiderByUserId(userId);
        if (existingRider != null) {
            throw new NotSupportException("该用户已注册为骑手");
        }

        // 检查手机号是否已被使用
        DeliveryRider phoneCheck = QueryChain.of(mapper)
                .where(DELIVERY_RIDER.PHONE.eq(deliveryRiderDTO.getPhone()))
                .one();
        if (phoneCheck != null) {
            throw new NotSupportException("该手机号已被其他骑手使用");
        }

        // 检查身份证号是否已被使用
        DeliveryRider idCardCheck = QueryChain.of(mapper)
                .where(DELIVERY_RIDER.ID_CARD.eq(deliveryRiderDTO.getIdCard()))
                .one();
        if (idCardCheck != null) {
            throw new NotSupportException("该身份证号已被其他骑手使用");
        }

        DeliveryRider rider = converter.deliveryDtoToDelivery(deliveryRiderDTO);
        rider.setUserId(userId);
        rider.setStatus(DeliveryRider.DeliveryRiderStatus.RESTING);
        rider.setRating(BigDecimal.valueOf(5.0));
        rider.setDeliveryCount(0);

        save(rider);
        return rider.getId();
    }

    /**
     * 检查用户是否已注册骑手
     *
     * @param userId 用户ID
     * @return 骑手信息，未注册返回null
     */
    @Override
    public DeliveryRider getRiderByUserId(Long userId) {
        return getOne(query().where(DELIVERY_RIDER.USER_ID.eq(userId)));

    }

    /**
     * 获取骑手信息
     *
     * @param userId 用户ID
     * @return 骑手信息
     */
    @Override
    public DeliveryRiderVO getRiderInfo(Long userId) {
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        return converter.deliveryRiderToDeliveryRiderVO(rider);
    }

    /**
     * 更新骑手状态
     *
     * @param userId 用户ID
     * @param status 状态
     */
    @Override
    @Transactional
    public void updateRiderStatus(Long userId, DeliveryRider.DeliveryRiderStatus status) {
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        rider.setStatus(status);
        updateById(rider);
    }

    /**
     * 更新骑手位置
     *
     * @param userId 用户ID
     * @param location 位置信息
     */
    @Override
    @Transactional
    public void updateRiderLocation(Long userId, String location) {
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        rider.setCurrentLocation(location);
        updateById(rider);
    }

    /**
     * 分页查询可接订单
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 可接订单列表
     */
    @Override
    public Page<AvailableOrderVO> getAvailableOrders(Long userId, int pageNumber, int pageSize) {
        // 验证骑手存在且状态为可接单
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        if (rider.getStatus() != DeliveryRider.DeliveryRiderStatus.AVAILABLE) {
            throw new InvalidParamException("骑手状态不允许接单", "status", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 查询已支付但未配送的订单
        Page<AvailableOrderVO> orderPage = new Page<>(pageNumber, pageSize);
        QueryWrapper query = orderService.queryChain()
                .select(
                        // 订单表字段
                        ORDER.ID,
                        ORDER.ORDER_NO,
                        ORDER.MERCHANT_ID,
                        ORDER.ADDRESS_ID,
                        ORDER.DELIVERY_FEE,
                        ORDER.REMARK,
                        ORDER.CREATED_AT,
                        ORDER.UPDATED_AT,

                        // 商家信息字段
                        MERCHANT.NAME.as("merchant_name"),
                        MERCHANT.PROVINCE.as("merchant_address_province"),
                        MERCHANT.CITY.as("merchant_address_city"),
                        MERCHANT.DISTRICT.as("merchant_address_district"),
                        MERCHANT.DETAIL_ADDRESS.as("merchant_address_detail_address"),

                        // 用户地址信息字段
                        USER_ADDRESS.PROVINCE.as("user_address_province"),
                        USER_ADDRESS.CITY.as("user_address_city"),
                        USER_ADDRESS.DISTRICT.as("user_address_district"),
                        USER_ADDRESS.DETAIL_ADDRESS.as("user_address_detail_address")
                )
                .leftJoin(MERCHANT)
                .on(ORDER.MERCHANT_ID.eq(MERCHANT.ID))
                .leftJoin(USER_ADDRESS)
                .on(USER_ADDRESS.ID.eq(ORDER.ADDRESS_ID))
                .leftJoin(DELIVERY_ORDER)
                .on(DELIVERY_ORDER.ORDER_ID.eq(ORDER.ID))
                .where(ORDER.STATUS.eq(Order.OrderStatus.ACCEPTED))
                .and(DELIVERY_ORDER.ID.isNull())
                .orderBy(ORDER.PAYMENT_TIME.asc());

        orderService.pageAs(orderPage, query, AvailableOrderVO.class);
        List<AvailableOrderVO> voList = getAvailableOrderVOS(orderPage);

        return new Page<>(voList, pageNumber, pageSize, orderPage.getTotalRow());
    }

    private List<AvailableOrderVO> getAvailableOrderVOS(Page<AvailableOrderVO> orderPage) {
        List<AvailableOrderVO> voList = orderPage.getRecords();
        voList.forEach(order -> {
            order.setMerchantAddress(
                    Stream.of(order.getMerchantAddressProvince(),
                            order.getMerchantAddressCity(),
                            order.getMerchantAddressDistrict(),
                            order.getMerchantAddressDetailAddress()
                            ).filter(Objects::nonNull)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining())
            );

            order.setDeliveryAddress(
                    Stream.of(order.getUserAddressProvince(),
                            order.getUserAddressCity(),
                            order.getUserAddressDistrict(),
                            order.getUserAddressDetailAddress()
                            ).filter(Objects::nonNull)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining())
            );
            order.setEstimatedDistance(BigDecimal.valueOf(2.5));
        });
        return voList;
    }

    /**
     * 骑手抢单
     *
     * @param userId 骑手用户ID
     * @param orderId 订单ID
     * @return 是否抢单成功
     */
    @Override
    @Transactional
    public boolean grabOrder(Long userId, Long orderId) {
        // 验证骑手存在且状态为可接单
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        if (rider.getStatus() != DeliveryRider.DeliveryRiderStatus.AVAILABLE) {
            throw new InvalidParamException("骑手状态不允许接单", "status", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 验证订单存在且状态为已支付
        Order order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessDataNotFoundException("订单不存在", orderId);
        }

        if (order.getStatus() != Order.OrderStatus.ACCEPTED) {
            throw new InvalidParamException("订单状态不允许接单", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 创建配送订单记录
        DeliveryOrder deliveryOrder = DeliveryOrder.builder()
                .orderId(orderId)
                .deliveryRiderId(rider.getId())
                .acceptTime(Timestamp.valueOf(LocalDateTime.now()))
                .status(DeliveryOrder.DeliveryOrderStatus.ACCEPTED)
                .build();
        deliveryOrderService.save(deliveryOrder);

        return true;
    }

    /**
     * 开始配送
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 是否开始成功
     */
    @Override
    @Transactional
    public boolean startDelivery(Long userId, Long orderId) {
        // 验证骑手存在
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        // 查询配送订单记录
        DeliveryOrder deliveryOrder = deliveryOrderService.getByOrderIdAndRiderId(orderId, rider.getId());

        if (deliveryOrder == null) {
            throw new InvalidParamException("该订单不属于当前骑手", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        if (deliveryOrder.getStatus() != DeliveryOrder.DeliveryOrderStatus.ACCEPTED) {
            throw new InvalidParamException("配送订单状态不允许开始配送", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 开始配送订单
        deliveryOrderService.startDelivery(deliveryOrder);

        // 更新订单状态为配送中
        Order order = orderService.getById(orderId);
        order.setStatus(Order.OrderStatus.DELIVERING);
        orderService.updateById(order);

        // 更新骑手状态为忙碌
        rider.setStatus(DeliveryRider.DeliveryRiderStatus.BUSY);
        updateById(rider);

        return true;
    }

    /**
     * 模拟送达
     *
     * @param userId 骑手用户ID
     * @param orderId 订单ID
     * @return 是否送达成功
     */
    @Override
    @Transactional
    public boolean completeDelivery(Long userId, Long orderId) {
        // 验证骑手存在
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        // 查询配送订单记录
        DeliveryOrder deliveryOrder = deliveryOrderService.getByOrderIdAndRiderId(orderId, rider.getId());

        if (deliveryOrder == null) {
            throw new InvalidParamException("该订单不属于当前骑手", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        if (deliveryOrder.getStatus() != DeliveryOrder.DeliveryOrderStatus.DELIVERING) {
            throw new InvalidParamException("配送订单状态不允许完成配送", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 完成配送订单
        deliveryOrderService.completeDelivery(deliveryOrder);

        // 更新订单状态为已完成
        Order order = orderService.getById(orderId);
        order.setStatus(Order.OrderStatus.COMPLETED);
        order.setDeliveryTime(Timestamp.valueOf(LocalDateTime.now()));
        orderService.updateById(order);

        // 更新骑手状态为可接单，增加配送次数
        rider.setStatus(DeliveryRider.DeliveryRiderStatus.AVAILABLE);
        rider.setDeliveryCount(rider.getDeliveryCount() + 1);
        updateById(rider);

        // 增加商家销量
        merchantService.updateMerchantSales(order.getMerchantId());

        return true;
    }

    /**
     * 查询骑手待配送的订单列表
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 骑手订单列表
     */
    @Override
    public Page<DeliveryOrderVO> getPendingDeliveryOrders(Long userId, int pageNumber, int pageSize) {
        // 验证骑手存在
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        // 分页查询骑手的配送订单
        Page<DeliveryOrderVO> orderPage = new Page<>(pageNumber, pageSize);
        QueryWrapper query = deliveryOrderService.queryChain()
                .select(
                        DELIVERY_ORDER.ALL_COLUMNS,
                        ORDER.TOTAL_AMOUNT,
                        ORDER.STATUS.as("order_status"),
                        MERCHANT.NAME.as("merchant_name"),
                        USER_ADDRESS.DETAIL_ADDRESS.as("delivery_address")
                )
                .leftJoin(ORDER).on(DELIVERY_ORDER.ORDER_ID.eq(ORDER.ID))
                .leftJoin(MERCHANT).on(ORDER.MERCHANT_ID.eq(MERCHANT.ID))
                .leftJoin(USER_ADDRESS).on(ORDER.ADDRESS_ID.eq(USER_ADDRESS.ID))
                .where(DELIVERY_ORDER.DELIVERY_RIDER_ID.eq(rider.getId()))
                .and(DELIVERY_ORDER.STATUS.eq(DeliveryOrder.DeliveryOrderStatus.ACCEPTED))
                .orderBy(DELIVERY_ORDER.ACCEPT_TIME.desc());

        deliveryOrderService.pageAs(orderPage, query, DeliveryOrderVO.class);
        return orderPage;
    }

    /**
     * 查询配送中订单
     *
     * @param userId     骑手用户ID
     * @param pageNumber 页码
     * @param pageSize   页面大小
     * @return 骑手配送中的订单列表
     */
    @Override
    public Page<DeliveryOrderVO> getDeliveringOrders(Long userId, int pageNumber, int pageSize) {
        // 验证骑手存在
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        // 分页查询骑手的配送中订单
        Page<DeliveryOrderVO> orderPage = new Page<>(pageNumber, pageSize);
        QueryWrapper query = deliveryOrderService.queryChain()
                .select(
                        DELIVERY_ORDER.ALL_COLUMNS,
                        ORDER.TOTAL_AMOUNT,
                        ORDER.STATUS.as("order_status"),
                        MERCHANT.NAME.as("merchant_name"),
                        USER_ADDRESS.DETAIL_ADDRESS.as("delivery_address")
                )
                .leftJoin(ORDER).on(DELIVERY_ORDER.ORDER_ID.eq(ORDER.ID))
                .leftJoin(MERCHANT).on(ORDER.MERCHANT_ID.eq(MERCHANT.ID))
                .leftJoin(USER_ADDRESS).on(ORDER.ADDRESS_ID.eq(USER_ADDRESS.ID))
                .where(DELIVERY_ORDER.DELIVERY_RIDER_ID.eq(rider.getId()))
                .and(DELIVERY_ORDER.STATUS.eq(DeliveryOrder.DeliveryOrderStatus.DELIVERING))
                .orderBy(DELIVERY_ORDER.ACCEPT_TIME.desc());

        deliveryOrderService.pageAs(orderPage, query, DeliveryOrderVO.class);
        return orderPage;
    }

    /**
     * 查询骑手配送完成的订单列表
     *
     * @param userId 骑手用户ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 骑手完成的订单列表
     */
    @Override
    public Page<DeliveryOrderVO> getCompletedOrders(Long userId, int pageNumber, int pageSize) {
        // 验证骑手存在
        DeliveryRider rider = getRiderByUserId(userId);
        if (rider == null) {
            throw new BusinessDataNotFoundException("用户未注册为骑手", userId);
        }

        // 分页查询骑手的已完成配送订单
        Page<DeliveryOrderVO> orderPage = new Page<>(pageNumber, pageSize);
        QueryWrapper query = deliveryOrderService.queryChain()
                .select(
                        DELIVERY_ORDER.ALL_COLUMNS,
                        ORDER.TOTAL_AMOUNT,
                        ORDER.STATUS.as("order_status"),
                        MERCHANT.NAME.as("merchant_name"),
                        USER_ADDRESS.DETAIL_ADDRESS.as("delivery_address")
                )
                .leftJoin(ORDER).on(DELIVERY_ORDER.ORDER_ID.eq(ORDER.ID))
                .leftJoin(MERCHANT).on(ORDER.MERCHANT_ID.eq(MERCHANT.ID))
                .leftJoin(USER_ADDRESS).on(ORDER.ADDRESS_ID.eq(USER_ADDRESS.ID))
                .where(DELIVERY_ORDER.DELIVERY_RIDER_ID.eq(rider.getId()))
                .and(DELIVERY_ORDER.STATUS.eq(DeliveryOrder.DeliveryOrderStatus.COMPLETED))
                .orderBy(DELIVERY_ORDER.COMPLETION_TIME.desc());

        deliveryOrderService.pageAs(orderPage, query, DeliveryOrderVO.class);
        return orderPage;
    }

}