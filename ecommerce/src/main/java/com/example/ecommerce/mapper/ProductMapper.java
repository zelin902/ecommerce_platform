package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.Product;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 随机抽取 limit 条商品
     */
    @Select("SELECT * FROM product ORDER BY RAND() LIMIT #{limit}")
    IPage<Product> randomProducts(Page<Product> page, @Param("limit") int limit);

    // 新增方法：根据ID获取商品详情
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product getById(@Param("id") Long id);

}