package com.zjw.springcloud.service;

import com.zjw.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

public interface PaymentService {
    int create(Payment payment);
    Payment getPaymentById(@Param("id")Long id);
}
