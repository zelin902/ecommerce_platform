package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.ProductDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductDetailMapper extends BaseMapper<ProductDetail> {
    void insertBatch(@Param("list") List<ProductDetail> list);
    @Select("SELECT field_name, field_value FROM product_detail WHERE product_id = #{productId}")
    List<ProductDetail> findSpecs(@Param("productId") Long productId);
}