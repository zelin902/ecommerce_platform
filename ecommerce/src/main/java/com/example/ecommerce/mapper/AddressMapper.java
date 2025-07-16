package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.Address;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AddressMapper extends BaseMapper<Address> {

    // 把该用户其它地址全部设为非默认
    @Update("UPDATE t_address SET is_default = 0 WHERE user_id = #{userId}")
    void clearDefault(@Param("userId") Long userId);

    // 设置默认
    @Update("UPDATE t_address SET is_default = 1 WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(@Param("id") Long id, @Param("userId") Long userId);
}