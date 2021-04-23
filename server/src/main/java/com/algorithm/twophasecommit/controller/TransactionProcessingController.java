package com.algorithm.twophasecommit.controller;

import com.algorithm.twophasecommit.TransactionProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 事务处理控制器
 */
@RestController
@RequestMapping("transactionProcessing")
public class TransactionProcessingController{

    @Autowired
    private TransactionProcessingService transactionProcessingService;

    /**
     * 获取事务ID信息
     * @param serviceNum
     * @return
     */
    @GetMapping("/getTransactionId")
    public Long getTransactionId(int serviceNum) {

        return transactionProcessingService.getTransactionId(serviceNum);
    }

    /**
     * 注册服务信息
     * @param id
     * @return
     */
    @GetMapping("/registerTransactionService")
    public void registerTransactionService(Long id) {
        transactionProcessingService.registerTransactionService(id);
    }

}
