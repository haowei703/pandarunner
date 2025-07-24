package com.shiroha.pandarunner.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shiroha.pandarunner.converter.OrderConverter;
import com.shiroha.pandarunner.domain.dto.OrderDTO;
import com.shiroha.pandarunner.domain.dto.OrderItemDTO;
import com.shiroha.pandarunner.domain.entity.*;
import com.shiroha.pandarunner.domain.vo.OrderItemVO;
import com.shiroha.pandarunner.domain.vo.OrderVO;
import com.shiroha.pandarunner.exception.BusinessDataConsistencyException;
import com.shiroha.pandarunner.exception.BusinessDataNotFoundException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.mapper.OrderMapper;
import com.shiroha.pandarunner.service.*;
import com.shiroha.pandarunner.util.OrderNumberGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.shiroha.pandarunner.domain.entity.table.OrderTableDef.ORDER;

/**
 * 订单表 服务层实现。
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderConverter converter;
    private final OrderItemService orderItemService;
    private final UserAddressService userAddressService;
    private final MerchantService merchantService;
    private final ProductService productService;
    private final SpecOptionService specOptionService;
    private final PaymentRecordService paymentRecordService;

    /**
     * 根据ID查询订单
     *
     * @param orderId 订单ID
     * @return vo对象
     */
    @Override
    public OrderVO getOrderById(Long userId, Long orderId) {
        Order order = getById(orderId);
        if(order == null) {
            throw new BusinessDataNotFoundException("订单ID不存在", orderId);
        }
        validateOrderBelongToUser(order, userId);

        OrderVO orderVO = converter.orderToOrderVO(order);
        orderVO.setItems(orderItemService.getItems(orderId));
        return orderVO;
    }

    /**
     * 新增订单
     *
     * @param userId   用户ID
     * @param orderDTO 订单DTO
     */
    @Override
    @Transactional
    public Map<String, Object> saveOrder(Long userId, OrderDTO orderDTO) {
        Long merchantId = orderDTO.getMerchantId();
        List<OrderItemDTO> dtoItems = orderDTO.getItems();

        Merchant merchant = merchantService.getMerchantById(merchantId, false);

        // 检验地址存在
        Long addressId = orderDTO.getAddressId();
        userAddressService.getUserAddressById(addressId);

        // 检验所有商品都属于该商家
        List<Long> productIds = dtoItems.stream()
                .map(OrderItemDTO::getProductId)
                .distinct()
                .toList();

        Map<Long, Product> productMap = productService.listByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        productService.validateProductsBelongToMerchant(merchantId, productMap.values());

        // 首先保存 order 获取 orderId
        Order order = converter.orderDtoToOrder(orderDTO);
        order.setUserId(userId);

        // 生成唯一订单号
        String orderNo = OrderNumberGenerator.generateOrderNumber(merchantId);
        order.setOrderNo(orderNo);
        save(order);

        // 构建 items 列表
        List<OrderItem> items = buildOrderItems(order, productMap, dtoItems);

        // 更新 order 对象
        updateOrder(order, merchant, items);

        // 更新表数据
        updateById(order);

        // 批量保存 items
        orderItemService.saveItems(items);

        Map<String, Object> result = new HashMap<>();
        result.put("order_id", order.getId());
        result.put("order_no", orderNo);
        return result;
    }

    /**
     * 更新订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @param orderDTO dto对象
     */
    @Override
    public void updateOrder(Long userId, Long orderId, OrderDTO orderDTO) {
        Order order = getById(orderId);
        if(order == null) {
            throw new BusinessDataNotFoundException("订单ID不存在", orderId);
        }
    }

    /**
     * 删除订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    @Override
    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if(order == null) {
            throw new BusinessDataNotFoundException("订单ID不存在", orderId);
        }

        validateOrderBelongToUser(order, userId);
        // 删除订单
        removeById(orderId);
        // 删除订单items
        List<Long> itemIds = orderItemService.getItems(orderId).stream()
                .map(OrderItemVO::getId)
                .toList();
        orderItemService.removeByIds(itemIds);
    }

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
    @Override
    public Page<OrderVO> getOrdersByPage(Long inquirerId, boolean isUser, int pageNumber, int pageSize, Order.OrderStatus status) {
        // 首先获取到订单的分页
        Page<Order> orderPage = new Page<>(pageNumber, pageSize);
        QueryWrapper query;
        if(isUser) {
            // 用户查询订单
            query = query().where(ORDER.USER_ID.eq(inquirerId));
        } else {
            // 商家查询订单，不包含未付款和已退款的订单
            query = query().where(ORDER.MERCHANT_ID.eq(inquirerId))
                    .and(ORDER.STATUS.notIn(Order.OrderStatus.PENDING_PAYMENT, Order.OrderStatus.REFUNDED));
        }

        if(status != null) {
            query.and(ORDER.STATUS.eq(status));
        }
        page(orderPage, query);

        // 获取到ID列表
        List<Order> orders = orderPage.getRecords();
        List<Long> orderIds = orders.stream().map(Order::getId).toList();

        if(orderIds.isEmpty()) {
            return new Page<>(List.of(), pageNumber, pageSize, 0);
        }

        // 初始化容器
        List<OrderVO> orderVOS = new ArrayList<>(orders.size());

        // 批量查询订单商品项
        Map<String, List<OrderItemVO>> itemsByOrderIds = orderItemService.getItemsByOrderIds(orderIds);

        // 数据填充
        for(Order order : orders) {
            OrderVO orderVO = converter.orderToOrderVO(order);
            orderVO.setItems(itemsByOrderIds.getOrDefault(String.valueOf(order.getId()), new ArrayList<>()));
            orderVOS.add(orderVO);
        }
        return new Page<>(orderVOS, pageNumber, pageSize, orderPage.getTotalRow());
    }

    /**
     * 检验订单属于用户
     *
     * @param order     订单
     * @param userId    用户ID
     */
    @Override
    public void validateOrderBelongToUser(Order order, Long userId) {
        // 检验订单是否属于用户
        if(!order.getUserId().equals(userId)) {
            throw new BusinessDataConsistencyException(
                    String.format("订单[%s]不属于用户[%s]", order.getId(), userId),
                    order.getId(),
                    userId,
                    BusinessDataConsistencyException.ErrorType.RELATION_MISMATCH);
        }
    }

    /**
     * 计算总价
     */
    private BigDecimal calculateOrderTotal(Merchant merchant, List<OrderItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            throw new InvalidParamException(
                    "订单商品项不能为空",
                    "items",
                    InvalidParamException.ErrorType.EMPTY_VALUE);
        }

        // 商品总价
        BigDecimal productTotal = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // TODO 折扣逻辑
        BigDecimal orderTotal = productTotal
                .add(merchant.getDeliveryFee())     // 配送费
                .add(merchant.getPackingFee());     // 打包费
        return orderTotal;
    }

    /**
     * 计算规格价格偏移总量
     */
    private BigDecimal calculateSpecOffsetTotal(Map<Long, SpecOption> specMap) {
        if (CollectionUtils.isEmpty(specMap)) {
            return BigDecimal.ZERO;
        }

        // 累加所有规格的价格偏移量
        return specMap.values().stream()
                .map(SpecOption::getPriceAdjust)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     *  构建订单项列表
     */
    private List<OrderItem> buildOrderItems(Order order, Map<Long, Product> productMap, List<OrderItemDTO> items) {
        return items.stream()
                .map(item -> {
                    // 获取商品基本信息
                    Product product = productMap.get(item.getProductId());
                    BigDecimal basePrice = product.getPrice();
                    BigDecimal itemPrice = basePrice;
                    String specText = null;

                    if(item.getSpec() != null) {
                        // 批量查询规格信息
                        Map<Long, SpecOption> specMap = specOptionService.listByIds(item.getSpec())
                                .stream()
                                .collect(Collectors.toMap(SpecOption::getId, Function.identity()));

                        // 验证所有规格属于该商品
                        specOptionService.validateSpecsBelongToProduct(item.getProductId(), specMap.values());

                        // 计算规格价格偏移总量
                        BigDecimal specOffsetTotal = calculateSpecOffsetTotal(specMap);
                        // 拼接规格信息
                        specText = specMap.values().stream()
                                .map(SpecOption::getName)
                                .collect(Collectors.joining("，"));

                        // 汇总item总价
                        itemPrice = basePrice.add(specOffsetTotal);
                    }

                    BigDecimal totalPrice = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    return OrderItem.builder()
                            .orderId(order.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .productImage(product.getImage())
                            .price(itemPrice)
                            .quantity(item.getQuantity())
                            .spec(specText)
                            .totalPrice(totalPrice)
                            .build();
                })
                .toList();
    }

    /**
     * 更新 order 对象
     */
    private void updateOrder(Order order, Merchant merchant, List<OrderItem> items) {
        // 计算订单总价
        BigDecimal totalAmount = calculateOrderTotal(merchant, items);
        order.setTotalAmount(totalAmount);
        order.setActualAmount(totalAmount);
        order.setDeliveryFee(merchant.getDeliveryFee());
        order.setPackingFee(merchant.getPackingFee());
        order.setStatus(Order.OrderStatus.PENDING_PAYMENT);
    }

    /**
     * 支付订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentMethod 支付方式
     * @return 支付流水号
     */
    @Override
    @Transactional
    public String payOrder(Long userId, Long orderId, PaymentRecord.PaymentMethod paymentMethod) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessDataNotFoundException("订单不存在", orderId);
        }

        validateOrderBelongToUser(order, userId);

        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new InvalidParamException("订单状态不允许支付", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // 创建支付记录
        Long paymentId = paymentRecordService.createPaymentRecord(orderId, paymentMethod);

        // 获取支付流水号
        PaymentRecord paymentRecord = paymentRecordService.getById(paymentId);
        return paymentRecord.getPaymentNo();
    }

    /**
     * 取消订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    @Override
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessDataNotFoundException("订单不存在", orderId);
        }

        validateOrderBelongToUser(order, userId);

        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT &&
                order.getStatus() != Order.OrderStatus.PAID) {
            throw new InvalidParamException("订单状态不允许取消", "orderId", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        updateById(order);
    }
}
