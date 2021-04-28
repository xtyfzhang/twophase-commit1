package com.algorithm.twophasecommit.service;

import com.algorithm.twophasecommit.annotation.TwoTC;
import com.algorithm.twophasecommit.feign.BOCBankFeignClient;
import com.algorithm.twophasecommit.mapper.ABCBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ABCBankService {

    @Autowired
    private ABCBankMapper abcBankMapper;

    @Autowired
    private BOCBankFeignClient bocBankFeignClient;

    /**
     * 转账:
     * @param ammount  大于0 ，转入；小于0，转出
     */
    @TwoTC
    public void transferOut(long ammount){

        // abc银行扣账
        abcBankMapper.transferable(ammount);
        // boc银行扣账
        bocBankFeignClient.transferInto(ammount);
    }

    /**
     * 取钱
     * @param ammount
     */
    @TwoTC
    public void takeMoney(long ammount){

        abcBankMapper.takeMoney(ammount);
    }
}
