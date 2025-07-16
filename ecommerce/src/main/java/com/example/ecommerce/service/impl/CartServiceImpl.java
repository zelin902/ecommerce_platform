// src/main/java/com/example/ecommerce/service/impl/CartServiceImpl.java
package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.domain.CartItem;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.web.vo.CartItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, CartItem> implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper; // 注入商品Mapper用于查询商品信息

    /**
     * 添加商品到购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 1. 校验商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在或已下架");
        }

        // 2. 校验库存
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足，当前库存：" + product.getStock());
        }

        // 3. 检查购物车中是否已有该商品
        CartItem existingItem = cartMapper.findByUserIdAndProductId(userId, productId);

        if (existingItem != null) {
            // 3.1 已存在：更新数量（原数量 + 新增数量）
            int newQuantity = existingItem.getQuantity() + quantity;
            // 再次校验更新后数量是否超过库存
            if (newQuantity > product.getStock()) {
                throw new RuntimeException("库存不足，最多可购买：" + product.getStock());
            }
            cartMapper.updateQuantity(userId, productId, newQuantity);
        } else {
            // 3.2 不存在：新增购物车记录
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice()); // 记录加入时的价格快照
            newItem.setSelected(true); // 默认选中
            cartMapper.insert(newItem);
        }
    }

    /**
     * 获取用户购物车列表（带商品详情）
     */
    @Override
    public List<CartItemVO> getUserCart(Long userId) {
        // 1. 查询用户购物车记录
        List<CartItem> cartItems = cartMapper.findByUserId(userId);
        if (cartItems.isEmpty()) {
            return List.of(); // 空列表
        }

        // 2. 关联商品信息，转换为VO（前端展示用）
        return cartItems.stream().map(cartItem -> {
            Product product = productMapper.selectById(cartItem.getProductId());
            CartItemVO vo = new CartItemVO();
            vo.setCartId(cartItem.getId());
            vo.setProductId(product.getId());
            vo.setProductName(product.getName());
            vo.setPrice(cartItem.getPrice()); // 使用加入时的价格快照
            vo.setQuantity(cartItem.getQuantity());
            vo.setStock(product.getStock());
            // 取第一张图片作为缩略图
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                vo.setProductImage(product.getImages().get(0));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 更新购物车商品数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        // 1. 校验购物车记录是否存在且属于当前用户
        CartItem cartItem = cartMapper.selectById(cartId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车记录不存在");
        }

        // 2. 校验数量合法性
        if (quantity <= 0) {
            throw new RuntimeException("数量不能小于1");
        }

        // 3. 校验库存
        Product product = productMapper.selectById(cartItem.getProductId());
        if (quantity > product.getStock()) {
            throw new RuntimeException("库存不足，最多可购买：" + product.getStock());
        }

        // 4. 更新数量
        cartMapper.updateQuantity(userId, cartItem.getProductId(), quantity);
    }

    /**
     * 从购物车移除商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long userId, Long cartId) {
        // 校验购物车记录是否属于当前用户
        CartItem cartItem = cartMapper.selectById(cartId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车记录不存在");
        }
        cartMapper.deleteById(cartId);
    }
}