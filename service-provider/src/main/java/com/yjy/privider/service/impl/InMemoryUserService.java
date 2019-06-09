package com.yjy.privider.service.impl;

import com.yjy.api.domain.User;
import com.yjy.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("inMemoryUserService")
public class InMemoryUserService implements UserService {

    private Map<Long,User> repository = new ConcurrentHashMap<>();


    @Override
    public boolean saveUser(User user) {
        return repository.put(user.getId(),user) == null;
    }

    @Override
    public List<User> findUserAll() {
        return new ArrayList(repository.values());
    }
}
