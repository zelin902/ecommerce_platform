package com.example.ecommerce.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("product_detail")
public class ProductDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long productId;
    private String fieldName;
    private String fieldValue;
}