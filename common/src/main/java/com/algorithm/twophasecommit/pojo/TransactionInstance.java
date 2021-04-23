package com.algorithm.twophasecommit.pojo;

import com.algorithm.twophasecommit.enums.TransactionStatus;
import lombok.Data;

/**
 * 事务实例
 */
@Data
public class TransactionInstance {

    /**
     * 事务唯一ID
     */
    private Long id;

    /**
     * 事务名称
     */
    private String name;

    /**
     * 事务状态
     */
    private TransactionStatus transactionStatus;
}
