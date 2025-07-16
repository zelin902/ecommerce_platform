package com.example.ecommerce.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName(value = "product", autoResultMap = true)   // ← 必须
public class Product {
    private Long id;
    private Long merchantId;
    private String name;
    private String service;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String review;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;
}