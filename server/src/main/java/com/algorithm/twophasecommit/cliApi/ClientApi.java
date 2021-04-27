package com.algorithm.twophasecommit.cliApi;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 服务端通知客户端接口
 */
//@FeignClient("two-phase-commit-client")
public interface ClientApi {

    /**
     * 询问执行结果，-1 ：执行失败；1：执行成功
     * @param id
     */
    @RequestLine("GET /askResult?id={id} ") //RequestMapping(value = "/askResult", method = RequestMethod.GET)
    int askResult(@Param("id") Long id);

    /**
     * 通知客户端事务提交
     * @param id
     */
    @RequestLine("POST /transactionCommit?id={id}")//@RequestMapping(value = "/transactionCommit",method = RequestMethod.POST)
    void transactionCommit(@Param("id")  Long id);

    /**
     * 通知客户端事务回滚
     * @param id
     */
    @RequestLine("POST /transactionRollBack?id={id}")//@RequestMapping(value = "POST /transactionRollBack",method = RequestMethod.POST)
    void transactionRollBack(@Param("id")  Long id);

}
