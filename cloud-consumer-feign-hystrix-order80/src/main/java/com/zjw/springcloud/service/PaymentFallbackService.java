package com.zjw.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfo_timeout(Integer id) {
        return "异常";
    }

    @Override
    public String paymentInfo_ok(Integer id) {
        return "正常";
    }
}
