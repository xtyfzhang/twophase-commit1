package com.algorithm.twophasecommit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.algorithm")
@EnableDiscoveryClient
public class TwoPhaseCommitApplication {

    public static void main(String[] args) {

        SpringApplication.run(TwoPhaseCommitApplication.class,args);
    }
}
