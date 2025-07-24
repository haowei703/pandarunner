package com.shiroha.pandarunner.controller;

import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.service.PaymentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "支付记录模块")
@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    /**
     * 支付订单
     *
     * @param paymentNo 支付流水号
     * @return 支付结果
     */
    @Operation(summary = "支付订单")
    @PostMapping("pay")
    public R<Boolean> payOrder(@RequestParam("payment_no") String paymentNo) {
        boolean result = paymentRecordService.simulatePaymentSuccess(paymentNo);
        return R.ok(result);
    }

}
