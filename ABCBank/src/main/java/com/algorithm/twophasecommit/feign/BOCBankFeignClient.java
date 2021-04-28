package com.algorithm.twophasecommit.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("boc-bank")
@RequestMapping("/bocbank")
public interface BOCBankFeignClient {


    /**
     * 转入
     * @param ammount
     */
    @PostMapping("/transferInto")
    public void  transferInto(@RequestParam("ammount") long ammount);

    /**
     * 转出
     * @param ammount
     */
    @PostMapping("/transferOut")
    public void transferOut(@RequestParam("ammount") long ammount);
}
