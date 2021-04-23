package com.algorithm.twophasecommit.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class TPCTransactioContextAware implements ApplicationContextAware {

    // spring上下文
    private  ApplicationContext applicationContext;

    // 事务管理器
    private  PlatformTransactionManager platformTransactionManager;

    // 事务ID
    private long transactioId;

    // 事务执行器
    private TransactionExecutor transactionExecutor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    public  PlatformTransactionManager getDefaultTransactionManager() throws BeansException {

        return (PlatformTransactionManager) applicationContext.getBean(PlatformTransactionManager.class);
    }

    public  void setTransactionManager(PlatformTransactionManager platformTransactionManager){

        this.platformTransactionManager = platformTransactionManager;
    }

    public long getTransactioId() {
        return transactioId;
    }

    public void setTransactioId(long transactioId) {
        this.transactioId = transactioId;
    }

    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }

    public void setTransactionExecutor(TransactionExecutor transactionExecutor) {
        this.transactionExecutor = transactionExecutor;
    }
}
