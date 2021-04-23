package com.algorithm.twophasecommit.enums;

/**
 * 事务状态信息
 */
public enum TransactionStatus {

    GETREADY,
    COMMI,
    ROLLBACK,
    END;
}
