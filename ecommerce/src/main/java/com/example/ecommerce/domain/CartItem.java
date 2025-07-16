package com.example.ecommerce.domain;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price; // 加入购物车时的价格快照
    private Boolean selected; // 是否选中结算
}