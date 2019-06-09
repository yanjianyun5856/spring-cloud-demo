package com.yjy.api.service;

import com.yjy.api.domain.User;
import com.yjy.api.fallback.UserServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name="${user.service.name}", fallback = UserServiceFallback.class) //申明feign客户端
public interface UserService {

    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping("/user/save")
    boolean saveUser(User user);

    @GetMapping("/user/find/all")
    List<User> findUserAll() throws InterruptedException;
}
