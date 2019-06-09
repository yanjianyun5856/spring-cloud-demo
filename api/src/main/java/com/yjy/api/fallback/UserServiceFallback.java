package com.yjy.api.fallback;

import com.yjy.api.domain.User;
import com.yjy.api.service.UserService;

import java.util.Collections;
import java.util.List;

public class UserServiceFallback implements UserService {
    @Override
    public boolean saveUser(User user) {
        return false;
    }

    @Override
    public List<User> findUserAll() {
        return Collections.emptyList();
    }
}
