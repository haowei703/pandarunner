package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.OrderDTO;
import com.shiroha.pandarunner.domain.entity.Order;
import com.shiroha.pandarunner.domain.entity.OrderItem;
import com.shiroha.pandarunner.domain.vo.OrderItemVO;
import com.shiroha.pandarunner.domain.vo.OrderVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderConverter {

    OrderDTO orderToOrderDTO(Order order);

    Order orderDtoToOrder(OrderDTO orderDTO);

    OrderVO orderToOrderVO(Order order);

    List<OrderVO> orderListToOrderVOList(List<Order> orders);

    OrderItemVO orderItemToOrderItemVO(OrderItem item);

    List<OrderItemVO> orderItemListToOrderItemVOList(List<OrderItem> items);
}