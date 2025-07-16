// src/main/java/com/example/ecommerce/web/CartController.java
package com.example.ecommerce.web;

import com.example.ecommerce.service.CartService;
import com.example.ecommerce.web.vo.CartItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 添加商品到购物车（商品详情页调用）
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {
        cartService.addToCart(userId, productId, quantity);
        return "加入购物车成功";
    }

    // 获取用户购物车列表（购物车页面调用）
    @GetMapping("/list")
    public List<CartItemVO> getUserCart(@RequestParam Long userId) {
        return cartService.getUserCart(userId);
    }

    // 更新购物车商品数量（购物车页面调用）
    @PostMapping("/update")
    public String updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long cartId,
            @RequestParam Integer quantity
    ) {
        cartService.updateQuantity(userId, cartId, quantity);
        return "数量更新成功";
    }

    // 移除购物车商品（购物车页面调用）
    @PostMapping("/remove")
    public String remove(@RequestParam Long userId, @RequestParam Long cartId) {
        cartService.remove(userId, cartId);
        return "商品已移除";
    }
}