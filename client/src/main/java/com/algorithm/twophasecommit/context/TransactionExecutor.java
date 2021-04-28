package com.algorithm.twophasecommit.context;

import com.algorithm.twophasecommit.api.ServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.locks.LockSupport;

@Component
@Slf4j
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

    private Thread lockThread;

    public TransactionExecutor(){
        lockThread = Thread.currentThread();
    }

    @Override
    public void run() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        try {

            // 执行切面方法
            excute();
            success = true;
            LockSupport.park(Thread.currentThread());
            if (operation == Operations.COMMIT) {
                log.warn("提交事务事务");
                platformTransactionManager.commit(transactionStatus);
            }
            if (operation == Operations.ROLLBACK) {
                log.warn("回滚事务");
                platformTransactionManager.rollback(transactionStatus);
            }
        } catch (Throwable throwable) {
            // 执行失败，进行回滚事务
            success = false;
            platformTransactionManager.rollback(transactionStatus);
            log.error("事务执行失败:{}",throwable.getMessage());
            log.warn("回滚事务");
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

    public Operations getOperation() {
        return operation;
    }

    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    public Thread getLockThread() {
        return lockThread;
    }

    public void setLockThread(Thread lockThread) {
        this.lockThread = lockThread;
    }
}
