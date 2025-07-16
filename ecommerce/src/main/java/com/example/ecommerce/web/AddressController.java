package com.example.ecommerce.web;

import com.example.ecommerce.domain.Address;
import com.example.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 从请求中获取userId（前端上传）
    @GetMapping
    public List<Address> list(@RequestParam Long userId) {
        return addressService.list(userId);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Address address) {
        addressService.save(address); // 直接使用前端传递的userId
        return ResponseEntity.ok().build();
    }

    // 设为默认地址
    @PutMapping("/default/{id}")
    public ResponseEntity<?> setDefault(@PathVariable Long id, @RequestParam Long userId) {
        // 打印接收的参数（替代 log）
        System.out.println("设置默认地址：id=" + id + ", userId=" + userId);
        addressService.setDefault(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam Long userId) {
        System.out.println("删除地址：id=" + id + ", userId=" + userId);
        addressService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok().build();
    }
}