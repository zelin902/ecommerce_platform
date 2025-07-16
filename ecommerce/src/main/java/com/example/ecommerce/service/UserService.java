package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void register(User user) {
        // 唯一校验
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        if (userMapper.selectByPhone(user.getPhone()) != null) {
            throw new IllegalArgumentException("手机号已存在");
        }
        // 加密
        user.setPassword(encoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

    public User login(String account, String rawPassword) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, account)
                .or()
                .eq(User::getEmail, account)
                .last("LIMIT 1");
        User user = userMapper.selectOne(wrapper);
        if (user == null || !encoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return user;
    }
}