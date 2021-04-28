package com.algorithm.twophasecommit.service;

import com.algorithm.twophasecommit.annotation.TwoTC;
import com.algorithm.twophasecommit.mapper.BOCBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BOCBankService {

    @Autowired
    private BOCBankMapper bocBankMapper;

    /**
     * 转账:
     * @param ammount  大于0 ，转入；小于0，转出
     */
    @TwoTC
    public void transferable(long ammount){

        bocBankMapper.transferable(ammount);
    }



    /**
     * 取钱
     * @param ammount
     */
    @TwoTC
    public void takeMoney(long ammount){

        if (ammount <= 0) {
            throw new RuntimeException("取钱金额为负");
        }
        bocBankMapper.takeMoney(ammount);
    }

    /**
     * 转入
     * @param ammount
     */
    @TwoTC
    public void  transferInto(long ammount){
       int count =  bocBankMapper.transferInto(ammount);
       if (count <= 0) {
           throw new RuntimeException("转入失败");
       }
    }

    /**
     * 转出
     * @param ammount
     */
    @TwoTC
    public void transferOut(long ammount){

        int count =  bocBankMapper.transferOut(ammount);
        if (count <= 0) {
            throw new RuntimeException("转出失败");
        }
    }
}
