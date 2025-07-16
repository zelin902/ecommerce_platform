package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.ProductDetail;
import com.example.ecommerce.mapper.ProductDetailMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.web.vo.ProductAddDTO;
import com.example.ecommerce.web.vo.ProductDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    private final ProductDetailMapper detailMapper;

    @Transactional
    public Integer add(ProductAddDTO dto) {
        // 1. 主表
        Product product = new Product();
        product.setMerchantId(dto.getMerchantId());
        product.setName(dto.getName());
        product.setService(dto.getService());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        product.setImages(dto.getImages());   // ← 保存图片地址
        this.save(product);
        Long productId = product.getId();

        // 2. 子表：过滤空白并转换字段名
        List<ProductDetail> details = dto.getDetails()
                .stream()
                .filter(d -> d.getKey() != null && !d.getKey().isBlank()
                        && d.getValue() != null && !d.getValue().isBlank())
                .map(d -> {
                    ProductDetail pd = new ProductDetail();
                    pd.setProductId(productId);
                    pd.setFieldName(d.getKey());
                    pd.setFieldValue(d.getValue());
                    return pd;
                })
                .collect(Collectors.toList());

        if (!details.isEmpty()) {
            detailMapper.insertBatch(details);
        }

        return productId.intValue();
    }
    // ★ 新增一行
    public List<Product> randomProducts(int limit) {
        return baseMapper.selectList(
                new QueryWrapper<Product>()
                        .orderBy(true, false, "RAND()")
                        .last("limit " + limit)
        );
    }
    public ProductDetailVO detail(Long id) {
        Product p = getById(id);
        if (p == null) return null;

        List<ProductDetail> details = detailMapper.findSpecs(id);
        Map<String, String> specs = details.stream()
                .collect(Collectors.toMap(ProductDetail::getFieldName, ProductDetail::getFieldValue));

        ProductDetailVO vo = new ProductDetailVO();
        vo.setId(p.getId());
        vo.setMerchantId(p.getMerchantId());
        vo.setName(p.getName());
        vo.setService(p.getService());
        vo.setCategory(p.getCategory());
        vo.setPrice(p.getPrice());
        vo.setStock(p.getStock());
        vo.setDescription(p.getDescription());
        vo.setReview(p.getReview());
        vo.setImages(p.getImages());
        vo.setSpecs(specs);
        return vo;
    }
}