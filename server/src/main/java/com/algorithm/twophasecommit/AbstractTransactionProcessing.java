package com.algorithm.twophasecommit;

import com.algorithm.twophasecommit.cliApi.ClientApi;
import com.algorithm.twophasecommit.enums.TransactionStatus;
import com.algorithm.twophasecommit.pojo.TransactionInstance;
import com.algorithm.twophasecommit.pojo.TransactionRegister;
import com.algorithm.twophasecommit.snowflow.SnowflakeIdWorker;
import com.algorithm.twophasecommit.utils.FeignUtils;
import com.algorithm.twophasecommit.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供默认实现事务处理信息
 */
public abstract class AbstractTransactionProcessing implements TransactionProcessing{

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
            TransactionRegister transactionRegister = new TransactionRegister();
            transactionRegister.setIp(IpUtils.getIp());
            transactionRegister.setPort(IpUtils.getLocalPort());
          //  transactionRegister.setServiceName(serviceName);

            List<TransactionRegister> transactionRegisters = map.getOrDefault(transactionid,new ArrayList<>());
            transactionRegisters.add(transactionRegister);
            map.put(transactionid,transactionRegisters);
            this.serviceNum = serviceNum;
            return transactionid;
        }
        return -1L;
    }

    /**
     * 注册事务信息
     * @return -1 存在执行中的事务，需要等待;
     */
    public void registerTransactionService(Long id) {

        TransactionRegister transactionRegister = new TransactionRegister();
        transactionRegister.setIp(IpUtils.getIp());
        transactionRegister.setPort(IpUtils.getLocalPort());
        List<TransactionRegister> transactionRegisters = map.getOrDefault(id,new ArrayList<>());
        transactionRegisters.add(transactionRegister);
        map.put(id,transactionRegisters);
        if (transactionRegisters.size() == serviceNum) {
            this.TransactionProcessing(id);
        }
    }

    /**
     * 判断是否存在已经执行的事务
     * @return
     */
    private boolean inActionTransaction(){

        if (transactionInstance.getTransactionStatus() == TransactionStatus.END) {
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

    /**
     * 事务处理方法
     * @param id
     */
    public void TransactionProcessing(Long id){

        int success = 0;
        int fail = 0;
        List<TransactionRegister> transactionInstances = map.get(id);
        for (TransactionRegister transactionRegister : transactionInstances) {
            ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getIp(),transactionRegister.getPort());
            int askResult = clientApi.askResult(id);
            if (askResult == -1) {
                fail += 1;
            }
            if (askResult == 1) {
                success += 1;
            }
        }
        // 回滚事务
        if (fail > 1) {
            for (TransactionRegister transactionRegister : transactionInstances) {
                ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getIp(),transactionRegister.getPort());
                 clientApi.transactionRollBack(id);
            }
        }
        // 提交事务
        if (fail == 0 && success == transactionInstances.size()) {
            for (TransactionRegister transactionRegister : transactionInstances) {
                ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getIp(),transactionRegister.getPort());
                clientApi.transactionCommit(id);
            }
        }

    }
}
