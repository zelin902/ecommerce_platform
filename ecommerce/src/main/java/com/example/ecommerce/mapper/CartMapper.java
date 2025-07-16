package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.CartItem;
import com.example.ecommerce.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<CartItem> {

    // 查询用户购物车
    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId}")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    // 查询用户是否已将某商品加入购物车
    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem findByUserIdAndProductId(@Param("userId") Long userId,
                                      @Param("productId") Long productId);

    // 更新商品数量
    @Update("UPDATE shopping_cart SET quantity = #{quantity}, updated_time = NOW() " +
            "WHERE user_id = #{userId} AND product_id = #{productId}")
    int updateQuantity(@Param("userId") Long userId,
                       @Param("productId") Long productId,
                       @Param("quantity") Integer quantity);
}