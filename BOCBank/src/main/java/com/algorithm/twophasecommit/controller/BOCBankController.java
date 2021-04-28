package com.algorithm.twophasecommit.controller;

import com.algorithm.twophasecommit.service.BOCBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bocbank")
public class BOCBankController {

    @Autowired
    private BOCBankService bocBankService;

    /**
     * 转账:
     * @param ammount  大于0 ，转入；小于0，转出
     */
    @PostMapping("/transferable")
    public void transferable(long ammount){

        bocBankService.transferable(ammount);
    }

    /**
     * 取钱
     * @param ammount
     */
    @PostMapping("takeMoney")
    public void takeMoney(long ammount){

        bocBankService.takeMoney(ammount);
    }

    /**
     * 转入
     * @param ammount
     */
    @PostMapping("transferInto")
    public void  transferInto(long ammount){
        bocBankService.transferInto(ammount);
    }

    /**
     * 转出
     * @param ammount
     */
    @PostMapping("transferOut")
    public void transferOut(long ammount){

        bocBankService.transferOut(ammount);
    }
}
