package com.algorithm.twophasecommit.controller;

import com.algorithm.twophasecommit.service.ABCBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abcbank")
public class ABCBankController {

    @Autowired
    private ABCBankService abcBankService;

    @PostMapping("/transferOut")
    public void transferOut(long ammount){

        abcBankService.transferOut(ammount);
    }
}
