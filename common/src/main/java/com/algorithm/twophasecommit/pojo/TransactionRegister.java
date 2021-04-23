package com.algorithm.twophasecommit.pojo;

import lombok.Data;

/**
 * 事务注册，用于存储注册事务的服务
 */
@Data
public class TransactionRegister {

    /**
     * 服务ID
     */
    private String ip;

    /**
     * 服务端口
     */
    private String port;
}
