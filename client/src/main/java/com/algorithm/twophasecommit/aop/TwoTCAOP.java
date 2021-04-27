package com.algorithm.twophasecommit.aop;

import com.algorithm.twophasecommit.api.ServiceApi;
import com.algorithm.twophasecommit.constant.Constants;
import com.algorithm.twophasecommit.context.TPCTransactioContextAware;
import com.algorithm.twophasecommit.context.TransactionExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.LockSupport;


/**
 * 切面TwoTC注解的信息
 */
@Aspect
@Component
public class TwoTCAOP {

    @Autowired
    private TPCTransactioContextAware tpcTransactioContextAware;

    @Autowired
    private ServiceApi serviceApi;

    // 以自定义 @TwoTC 注解为切点
    @Pointcut("@annotation(com.algorithm.twophasecommit.annotation.TwoTC)")
    public void twoTc() {
    }

    /**
     * 处理分布式事务
     * @param proceedingJoinPoint
     * @throws
     */
    @Around("twoTc()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 获取事务管理器
        PlatformTransactionManager tm = tpcTransactioContextAware.getDefaultTransactionManager();
        tpcTransactioContextAware.setTransactionManager(tm);
        // 获取事务ID信息
        Long traId = null;
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String trId = request.getHeader(Constants.TPC_TRANSACTION_ID);
        if (trId == null || trId == "") {
            traId = serviceApi.getTransactionId(1);
        } else {
            traId = Long.valueOf(trId);
        }
        // 存在有效事务未提交，则等待一段时间重新提交
        while (traId == -1){
            // 等待一段时间之后自动获取事务ID信息
            Thread.sleep(100);
            traId = serviceApi.getTransactionId(0);
        }
        // 注册服务
        serviceApi.registerTransactionService(traId);
        // 保存事务ID信息
        tpcTransactioContextAware.setTransactioId(traId);
        TransactionExecutor transactionExecutor = new TransactionExecutor(){
            @Override
            public void excute() throws Throwable{
                Object obj = proceedingJoinPoint.proceed();
                this.setExcuteResult(obj);
            }
        };
        transactionExecutor.setPlatformTransactionManager(tm);
        transactionExecutor.start();
        //将设置到相关的信息
        tpcTransactioContextAware.setTransactionExecutor(transactionExecutor);
        // 阻塞当前线程，等待其它线程执行情况，执行事务完成后释放事务
        LockSupport.park(Thread.currentThread());
        return transactionExecutor.getExcuteResult();
    }

}
