// src/main/java/com/example/ecommerce/web/vo/CartItemVO.java
package com.example.ecommerce.web.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long cartId;         // 购物车记录ID
    private Long productId;      // 商品ID
    private String productName;  // 商品名称
    private BigDecimal price;    // 商品价格
    private Integer quantity;    // 购买数量
    private Integer stock;       // 商品库存
    private String productImage; // 商品图片
}