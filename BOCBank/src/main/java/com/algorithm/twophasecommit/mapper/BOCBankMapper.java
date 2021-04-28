package com.algorithm.twophasecommit.mapper;

public interface BOCBankMapper {

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

    /**
     * 转入
     * @param ammount
     */
    int  transferInto(long ammount);

    /**
     * 转出
     * @param ammount
     */
    int transferOut(long ammount);
}
