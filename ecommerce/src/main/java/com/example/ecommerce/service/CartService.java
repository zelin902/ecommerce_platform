// src/main/java/com/example/ecommerce/service/CartService.java
package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.domain.CartItem;
import com.example.ecommerce.web.vo.CartItemVO;

import java.util.List;

public interface CartService extends IService<CartItem> {
    // 添加商品到购物车
    void addToCart(Long userId, Long productId, Integer quantity);

    // 获取用户购物车列表（带商品详情）
    List<CartItemVO> getUserCart(Long userId);

    // 更新购物车商品数量
    void updateQuantity(Long userId, Long cartId, Integer quantity);

    // 从购物车移除商品
    void remove(Long userId, Long cartId);
}