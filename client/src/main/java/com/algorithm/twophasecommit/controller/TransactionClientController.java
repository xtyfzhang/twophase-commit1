package com.algorithm.twophasecommit.controller;

import com.algorithm.twophasecommit.context.TPCTransactioContextAware;
import com.algorithm.twophasecommit.context.TransactionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.LockSupport;

@RestController
public class TransactionClientController {

    @Autowired
    private TPCTransactioContextAware tpcTransactioContextAware;
    /**
     * 询问执行结果，-1 ：执行失败；1：执行成功
     * @param id
     */
    @GetMapping("/askResult")
    int askResult(@RequestParam Long id){

        // 获取执行结果
        boolean excute = tpcTransactioContextAware.getTransactionExecutor().isSuccess();
        return excute ? 1 : -1;
    }

    /**
     * 通知客户端事务提交
     * @param id
     */
    @PostMapping("/transactionCommit")
    void transactionCommit(@RequestParam  Long id){
        // 获取事务管理器
      //  PlatformTransactionManager platformTransactionManager = tpcTransactioContextAware.getDefaultTransactionManager();
        TransactionExecutor transactionExecutor = tpcTransactioContextAware.getTransactionExecutor();
        // 提交事务
        transactionExecutor.setOperation(TransactionExecutor.Operations.COMMIT);
        LockSupport.unpark(transactionExecutor);
       // platformTransactionManager.commit(transactionStatus);

    }

    /**
     * 通知客户端事务回滚
     * @param id
     */
    @PostMapping("/transactionRollBack")
    void transactionRollBack(@RequestParam  Long id){

      //  PlatformTransactionManager platformTransactionManager = tpcTransactioContextAware.getDefaultTransactionManager();
        TransactionExecutor transactionExecutor = tpcTransactioContextAware.getTransactionExecutor();
        // 提交事务
        transactionExecutor.setOperation(TransactionExecutor.Operations.ROLLBACK);
        LockSupport.unpark(transactionExecutor);
    }
}
