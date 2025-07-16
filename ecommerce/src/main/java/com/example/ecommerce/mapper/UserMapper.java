package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

@Mapper   // <-- 就缺这一行
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM t_user WHERE username = #{username} LIMIT 1")
    User selectByUsername(String username);

    @Select("SELECT * FROM t_user WHERE email = #{email} LIMIT 1")
    User selectByEmail(String email);

    @Select("SELECT * FROM t_user WHERE phone = #{phone} LIMIT 1")
    User selectByPhone(String phone);
}