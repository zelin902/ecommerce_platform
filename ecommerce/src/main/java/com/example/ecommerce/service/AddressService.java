package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ecommerce.domain.Address;
import com.example.ecommerce.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;

    public List<Address> list(Long userId) {
        LambdaQueryWrapper<Address> wrapper = Wrappers.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime);
        return addressMapper.selectList(wrapper);
    }

    @Transactional
    public void save(Address address) {
        if (address.getIsDefault() == null) address.setIsDefault(false);
        if (address.getIsDefault()) {
            addressMapper.clearDefault(address.getUserId());
        }
        if (address.getId() == null) {
            addressMapper.insert(address);
        } else {
            addressMapper.updateById(address);
        }
    }

    @Transactional
    public void delete(Long id, Long userId) {
        addressMapper.delete(
                Wrappers.lambdaQuery(Address.class)
                        .eq(Address::getId, id)
                        .eq(Address::getUserId, userId));
    }

    @Transactional
    public void setDefault(Long id, Long userId) {
        addressMapper.clearDefault(userId);
        int rows = addressMapper.setDefault(id, userId); // 获取影响行数
        System.out.println("setDefault 影响行数：" + rows); // 必须为 1 才生效
        if (rows == 0) {
            throw new RuntimeException("地址不存在或不属于当前用户");
        }
    }
}