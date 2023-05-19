package com.zjw.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    /**
     * 正常访问
     *
     * @param id
     * @return
     */
    public String paymentInfo_ok(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_ok,id:" + id+"\n"+"2040129511钟佳炜";
    }

    /**
     * 延时访问
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_timeoutHandler", commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "5000")//设置超时/报错启动Hystrix
    })//兜底方法
    public String paymentInfo_timeout(Integer id) {
        //设置超时报出异常
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_timeout,id:" + id;
    }

    public String paymentInfo_timeoutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "paymentInfo_timeoutHandler,id:" + id;
    }

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数(10秒内20次)
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少后跳闸（10秒内超过50%的请求）
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号:" + serialNumber;
    }
    public String paymentCircuitBreaker_fallback (@PathVariable("id") Integer id){
        return "id 不能负数，请稍后再试，/(ToT)/~~ id: " + id;
    }
}