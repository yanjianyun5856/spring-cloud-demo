package com.yjy.client.controller;

import com.yjy.api.domain.User;
import com.yjy.client.hystrix.RibbonClientHystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

@RestController
public class UserRibbonController {
    private final static Random random = new Random();

    /**
     * 负载均衡器客户端
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${service.provide.name}")
    private String serviceProviderName;

    @Autowired
    private RestTemplate restTemplate;

    private RibbonClientHystrixCommand hystrixCommand;

    @GetMapping("/index")
    public String index() throws IOException {

        User user = new User();
        user.setId(1);
        user.setName("yjy");

        //选择指定的serviceId
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceProviderName);
        return loadBalancerClient.execute(serviceProviderName,serviceInstance,
                instance ->{
                    String host = instance.getHost();
                    int port = instance.getPort();
                    String url = "http://" + host + ":" + port + "/user/save";
                    RestTemplate restTemplate = new RestTemplate();
                    return restTemplate.postForObject(url,user,String.class);
                }
        );
    }

    @GetMapping("/user/list")
    public Collection<User> getUserList() throws InterruptedException {
        int executeTime = random.nextInt(200);

        //System.out.println("休眠时间：" + executeTime);
        //Thread.sleep(executeTime);//休眠时间

        //return restTemplate.getForObject("http://"+serviceProviderName+"/user/list",Collection.class);
        return new RibbonClientHystrixCommand(serviceProviderName,restTemplate).execute();
    }


}
