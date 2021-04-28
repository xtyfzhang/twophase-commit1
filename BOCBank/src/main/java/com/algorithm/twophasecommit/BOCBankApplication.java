package com.algorithm.twophasecommit;

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
@MapperScan("com.algorithm.twophasecommit.mapper")
public class BOCBankApplication {

    public static void main(String[] args) {

        SpringApplication.run(BOCBankApplication.class,args);
    }
}
