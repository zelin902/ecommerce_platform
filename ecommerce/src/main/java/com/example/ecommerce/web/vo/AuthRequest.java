package com.example.ecommerce.web.vo;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String phone,
        @NotBlank String password,
        String confirmPassword,
        Integer type   // 0=用户 1=商家
) {}