package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.service.DeliveryOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "配送订单模块")
@RestController
@RequestMapping("/delivery/order")
@AllArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;



}
