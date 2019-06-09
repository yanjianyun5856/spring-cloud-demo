package com.yjy.client;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.yjy.api.service.UserService;
import com.yjy.client.ping.MyPing;
import com.yjy.client.rule.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RibbonClients({
        @RibbonClient(name = "service-provider")
})
@EnableCircuitBreaker //使用服务短路
@EnableFeignClients(clients = UserService.class) //申明 UserService 接口作为 feign client 调用
@EnableDiscoveryClient
public class ServiceClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceClientApplication.class,args);
    }

    @Bean
    public IRule myRule(){
        return new MyRule();
    }

    @Bean
    public IPing myPing(){
        return new MyPing();
    }


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
