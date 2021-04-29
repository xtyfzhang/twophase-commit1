package com.algorithm.twophasecommit;

import com.algorithm.twophasecommit.cliApi.ClientApi;
import com.algorithm.twophasecommit.enums.TransactionStatus;
import com.algorithm.twophasecommit.pojo.TransactionInstance;
import com.algorithm.twophasecommit.pojo.TransactionRegister;
import com.algorithm.twophasecommit.utils.FeignUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class AsynTransactionExecute extends Thread{

    // 服务信息
    private Map<Long, List<TransactionRegister>> map;

    //事务实例信息
    private TransactionInstance transactionInstance;

    // 事务ID
    private Long traId;

    public AsynTransactionExecute(Map<Long, List<TransactionRegister>> map,TransactionInstance transactionInstance,Long traId){

        this.map = map;
        this.transactionInstance = transactionInstance;
        this.traId = traId;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int success = 0;
        int fail = 0;
        List<TransactionRegister> transactionInstances = map.get(traId);
        if (transactionInstances == null) {
            return ;
        }
        for (TransactionRegister transactionRegister : transactionInstances) {
            ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getAddr());
            int askResult = clientApi.askResult(traId);
            if (askResult == -1) {
                fail += 1;
            }
            if (askResult == 1) {
                success += 1;
            }
        }
        // 回滚事务
        if (fail >= 1 || success < transactionInstances.size()) {
            for (TransactionRegister transactionRegister : transactionInstances) {
                //log.info();
                log.info("通知客户端{}事务执行失败，回滚事务",transactionRegister.getAddr());
               ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getAddr());
               clientApi.transactionRollBack(traId);
            }
        }
        // 提交事务
        if (fail == 0 && success == transactionInstances.size()) {
            for (TransactionRegister transactionRegister : transactionInstances) {
                log.info("通知知客户端{}事务执行成功，提交事务",transactionRegister.getAddr());
                ClientApi clientApi = FeignUtils.createFeignService(transactionRegister.getAddr());
                clientApi.transactionCommit(traId);
            }
        }
        map.clear();
        transactionInstance.setTransactionStatus(TransactionStatus.END);
    }
}
