package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.io.File;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.ecommerce.mapper")
@ComponentScan(basePackages = {
        "com.example.ecommerce",
        "com.example.ecommerce.config" // 确保包含处理器所在包
})
public class EcommerceApplication {

    @PostConstruct
    public void init() {
        File dir = new File("./upload/");
        if (!dir.exists()) dir.mkdirs();
    }

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}