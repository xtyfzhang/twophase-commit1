package com.algorithm.twophasecommit;

import com.algorithm.twophasecommit.cliApi.ClientApi;
import com.algorithm.twophasecommit.enums.TransactionStatus;
import com.algorithm.twophasecommit.pojo.TransactionInstance;
import com.algorithm.twophasecommit.pojo.TransactionRegister;
import com.algorithm.twophasecommit.snowflow.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供默认实现事务处理信息
 */
public abstract class AbstractTransactionProcessing {

    @Autowired
    private  SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 注册信息
     */
    private Map<Long, List<TransactionRegister>> map = new ConcurrentHashMap<>();

    /**
     * 执行事务实例,整体事务处理器
     */
    private TransactionInstance transactionInstance = new TransactionInstance();

    /**
     * 注册服务的数量
     */
    private int serviceNum = 0;

    /**
     * 获取事务Id
     * @param serviceNum
     * @return
     */
    public Long  getTransactionId(int serviceNum){
        // 查询是否存在执行中的事务
        if (!inActionTransaction()) {
            long transactionid  = snowflakeIdWorker.nextId();
            this.serviceNum = serviceNum;
            // 执行询问程序
            return transactionid;
        }
        return -1L;
    }

    /**
     * 注册事务信息
     * @return -1 存在执行中的事务，需要等待;
     */
    public void registerTransactionService(Long id,String serverAddr) {

        TransactionRegister transactionRegister = new TransactionRegister();
        transactionRegister.setAddr(serverAddr);
        List<TransactionRegister> transactionRegisters = map.getOrDefault(id,new ArrayList<>());
        transactionRegisters.add(transactionRegister);
        map.put(id,transactionRegisters);
        if (transactionRegisters.size() == serviceNum) {
            // 执行事务处理
            AsynTransactionExecute asynTransactionExecute = new AsynTransactionExecute(map,transactionInstance,id);
            asynTransactionExecute.start();
        }
    }

    /**
     * 判断是否存在已经执行的事务
     * @return
     */
    private boolean inActionTransaction(){

        if (transactionInstance.getTransactionStatus() == null || transactionInstance.getTransactionStatus() == TransactionStatus.END) {
            return false;
        }
        return true;
    }

    public TransactionInstance getTransactionInstance() {
        return transactionInstance;
    }

    public void setTransactionInstance(TransactionInstance transactionInstance) {
        this.transactionInstance = transactionInstance;
    }

}
