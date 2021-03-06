package com.algorithm.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.algorithm")
@EnableFeignClients(basePackages = "com.algorithm")
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan("com.algorithm.example.mapper")
public class ExampleApplication {

    public static void main(String[] args) {

        SpringApplication.run(ExampleApplication.class);
    }
}
