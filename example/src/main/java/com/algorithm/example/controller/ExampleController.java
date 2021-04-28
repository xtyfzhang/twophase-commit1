package com.algorithm.example.controller;

import com.algorithm.example.mapper.ExampleMapper;
import com.algorithm.twophasecommit.annotation.TwoTC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private ExampleMapper exampleMapper;

    @RequestMapping("/test")
    @TwoTC
    public void test(){
        exampleMapper.insert();
      //  throw new RuntimeException("==测试事务执行==");
    }
}
