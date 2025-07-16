package com.example.ecommerce.web;

import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.web.vo.ProductAddDTO;
import com.example.ecommerce.web.vo.ProductDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce.domain.Product;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    @PostMapping("/goods/add")
    public Integer add(@RequestBody ProductAddDTO dto) {
        return productService.add(dto);
    }
    @GetMapping("/random")
    public List<Product> randomProducts(@RequestParam(defaultValue = "5") int limit) {
        return productService.randomProducts(Math.min(limit, 50));
    }
    @GetMapping("/product/{id}")
    public ProductDetailVO detail(@PathVariable Long id) {
        ProductDetailVO vo = productService.detail(id);
        if (vo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品不存在");
        return vo;
    }
}