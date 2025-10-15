package com.ecom.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 电商管理系统主启动类
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.ecom.management.mapper")
public class EcomManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomManagementApplication.class, args);
    }
}
