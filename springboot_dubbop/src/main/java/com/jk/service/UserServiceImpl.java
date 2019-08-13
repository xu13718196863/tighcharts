package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.dao.UserDao;
import com.jk.model.User;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements UserService {
      @Autowired
      private UserDao us;

    //登录
    @Override
    public User denglu(String name) {
        return us.denglu(name);
    }



}
