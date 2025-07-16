package com.example.ecommerce.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
    private Long id;
    private String username;
    private Integer gender;
    private java.time.LocalDate birthday;
    private String phone;
    private String email;
    private String realName;
    private String idCard;
    private String password;
    private String avatar;
    private Integer userType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
