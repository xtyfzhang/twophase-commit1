package com.algorithm.twophasecommit.mapper;

public interface ABCBankMapper {

    /**
     * 转账:
     * @param ammount  大于0 ，转入；小于0，转出
     */
    public void transferable(long ammount);

    /**
     * 取钱
     * @param ammount
     */
    void takeMoney(long ammount);
}
