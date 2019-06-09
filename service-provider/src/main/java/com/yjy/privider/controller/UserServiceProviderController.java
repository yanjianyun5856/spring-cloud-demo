package com.yjy.privider.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yjy.api.domain.User;
import com.yjy.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
//@RequestMapping("/user")
public class UserServiceProviderController  implements  UserService{

    private final static Random random = new Random();

    @Autowired
    @Qualifier("inMemoryUserService")
    private UserService userService;

    @Autowired
    private JmsTemplate jmsTemplate;
    public Object pollUser(){

        // 获取消息队列中的消息 默认 Destination = TEST
        return jmsTemplate.receiveAndConvert();
    }

    //@PostMapping("/save")
    //通过方法继承 URI 映射
    @Override
    public boolean saveUser(@RequestBody User user){

        return userService.saveUser(user);
    }
    @HystrixCommand(
            commandProperties = {// command 配置
                    //设置超时时间100 毫秒
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            },
            fallbackMethod = "fallbackForGetUsers" //设置 fallback 方法
    )
    @Override
    public List<User> findUserAll() throws InterruptedException {
        int executeTime = random.nextInt(200);

        System.out.println("休眠时间：" + executeTime);
        Thread.sleep(executeTime);//休眠时间

        return userService.findUserAll();
    }

    @GetMapping("/list")
    @HystrixCommand(
            commandProperties = {// command 配置
                    //设置超时时间100 毫秒
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            },
            fallbackMethod = "fallbackForGetUsers" //设置 fallback 方法
    )
    public List<User> getUserList() throws InterruptedException {
        int executeTime = random.nextInt(200);

        System.out.println("休眠时间：" + executeTime);
        Thread.sleep(executeTime);//休眠时间

        return userService.findUserAll();
    }

    public List<User> fallbackForGetUsers(){

        return Collections.emptyList();
    }
}
