package com.warehouse.inout;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.warehouse.inout")
@MapperScan("com.warehouse.inout.mapper")
@ComponentScan(basePackages = {"com.warehouse.inout", "com.warehouse.common"})
public class InoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(InoutApplication.class, args);
    }
}
