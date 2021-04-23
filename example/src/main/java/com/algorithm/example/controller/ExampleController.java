package com.algorithm.example.controller;

import com.algorithm.twophasecommit.annotation.TwoTC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/example")
public class ExampleController {

    @RequestMapping("/test")
    //@TwoTC
    public void test(){
        throw new RuntimeException("测试事务执行");
    }
}
