package com.zjw.springcloud.controller;

import com.zjw.springcloud.entities.CommonResult;
import com.zjw.springcloud.entities.Payment;
import com.zjw.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService service;

    @Value("${server.port}")
    private String servicePort;

    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping("/payment/create")
    public CommonResult creat(@RequestBody Payment payment){
        int result = service.create(payment);
        log.info("插入结果:"+result);
        if (result>0){
            return new CommonResult(200,"插入数据成功,servicePort:"+servicePort,result);
        }else {
            return new CommonResult(444,"插入失败",null);
        }
    }

    @GetMapping("/payment/select/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = service.getPaymentById(id);
        log.info("插入结果:"+payment);
        if (payment!=null){
            return new CommonResult(200,"查询成功,servicePort:"+servicePort,payment);
        }else {
            return new CommonResult(444,"没有对应记录，查询id："+id,null);
        }
    }
    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();//获取所有服务
        for (String element:services){
            log.info("****element:"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVER");//根据名称获取服务
        for (ServiceInstance instance:instances){
            log.info(instance.getServiceId()+"\t"
                    +instance.getHost()+"\t"
                    +instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }
    @GetMapping("/payment/lb")
    public String getPayment(){
        return "自定义轮询："+servicePort;//获取当前端口号
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);//模拟处理时间为3秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return servicePort;
    }
}
