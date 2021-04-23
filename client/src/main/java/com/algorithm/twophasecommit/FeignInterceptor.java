package com.algorithm.twophasecommit;

import com.algorithm.twophasecommit.constant.Constants;
import com.algorithm.twophasecommit.context.TPCTransactioContextAware;
import feign.RequestInterceptor;
import org.springframework.stereotype.Component;

/**
 * Transfer the transaction id and timeout through header.
 * 
 * @author John.Huang
 */
@Component
public class FeignInterceptor implements RequestInterceptor{

	private TPCTransactioContextAware tpcTransactioContextAware;
	@Override
	public void apply(feign.RequestTemplate requestTemplate) {

		Long tid = tpcTransactioContextAware.getTransactioId();
		if (tid != null) {
			// 设置事务ID
			requestTemplate.header(Constants.TPC_TRANSACTION_ID, tid + "");
		}
	}
}