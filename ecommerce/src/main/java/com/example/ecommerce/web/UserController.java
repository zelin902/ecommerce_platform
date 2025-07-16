package com.example.ecommerce.web;

import com.example.ecommerce.domain.User;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.web.vo.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // 注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPhone(req.phone());
        user.setPassword(req.password());
        user.setUserType(req.type() != null ? req.type() : 0);
        userService.register(user);
        return ResponseEntity.ok().body("注册成功");
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        User user = userService.login(req.username(), req.password());
        return ResponseEntity.ok().body(Map.of("id", user.getId().toString()));
    }
}