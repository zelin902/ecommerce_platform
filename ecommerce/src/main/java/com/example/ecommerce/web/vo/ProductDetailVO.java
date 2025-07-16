package com.example.ecommerce.web.vo;

import com.example.ecommerce.domain.ProductDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ProductDetailVO {
    private Long id;
    private Long merchantId;
    private String name;
    private String service;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String review;
    private List<String> images;
    private Map<String, String> specs;   // 来自 product_detail 表
}