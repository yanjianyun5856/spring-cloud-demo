package com.yjy.privider;

import com.yjy.privider.stream.UserMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableHystrix
@EnableDiscoveryClient
@EnableBinding(UserMessage.class) //激活 stream binding 到 UserMessage
public class ServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProviderApplication.class,args);
    }
}
