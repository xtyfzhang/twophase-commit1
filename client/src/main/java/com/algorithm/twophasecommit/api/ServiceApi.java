package com.algorithm.twophasecommit.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 调用服务端接口西悉尼
 */
@FeignClient("two-phase-commit-service")
@RequestMapping("transactionProcessing")
public interface ServiceApi {

    /**
     * 获取事务ID
     * @param serviceNum
     * @return
     */
    @GetMapping("/getTransactionId")
    public Long  getTransactionId(@RequestParam("serviceNum") int serviceNum);

    /**
     * 注册事务服务
     * @return 事务ID
     */
    @GetMapping("/registerTransactionService")
    public Long registerTransactionService(@RequestParam("id") Long id);

}
