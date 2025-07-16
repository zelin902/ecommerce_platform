package com.example.ecommerce.web.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductAddDTO {
    private String name;
    private String service;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private List<DetailDTO> details;
    private List<String> images;
    private Long merchantId;
}