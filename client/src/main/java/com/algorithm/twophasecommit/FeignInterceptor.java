package com.algorithm.twophasecommit;

import com.algorithm.twophasecommit.constant.Constants;
import com.algorithm.twophasecommit.context.TPCTransactioContextAware;
import com.algorithm.twophasecommit.utils.IpUtils;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Transfer the transaction id and timeout through header.
 * 
 * @author John.Huang
 */
@Component
public class FeignInterceptor implements RequestInterceptor{

	@Autowired
	private TPCTransactioContextAware tpcTransactioContextAware;
	@Override
	public void apply(feign.RequestTemplate requestTemplate) {
		// 添加服务信息
		Long tid = tpcTransactioContextAware.getTransactioId();
		if (tid != null) {
			// 设置事务ID
			requestTemplate.header(Constants.TPC_TRANSACTION_ID, tid + "");
		}
		requestTemplate.header(Constants.SERVER_ADDR, IpUtils.getLocalIP()+":"+IpUtils.getLocalPort());
	}
}