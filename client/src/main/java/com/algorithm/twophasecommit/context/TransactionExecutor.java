package com.algorithm.twophasecommit.context;

import com.algorithm.twophasecommit.api.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionExecutor extends Thread{

    private final static String ERROR_KEY = "ERROR";
    public enum Operations {
        COMMIT, ROLLBACK
    }
    private Operations operation = Operations.ROLLBACK;

    // 执行结果
    private Object excuteResult;

    // 事务ID
    private long transactionId = 0;

    @Autowired
    private ServiceApi serviceApi;

    private boolean success = false;

    // 设置事务信息
    private PlatformTransactionManager platformTransactionManager;

    // 事务状态
    private TransactionStatus transactionStatus;

    @Override
    public void run() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            // 执行切面方法
            excute();
            // 执行成功，进行提交事务
            success = true;
          //  serviceApi.commit(transactionId);
        } catch (Throwable throwable) {
            // 执行失败，进行回滚事务
            success = false;
            throwable.printStackTrace();
        }
    }

    public void excute() throws Throwable{

    }

    public Object getExcuteResult() {
        return excuteResult;
    }

    public void setExcuteResult(Object excuteResult) {
        this.excuteResult = excuteResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
