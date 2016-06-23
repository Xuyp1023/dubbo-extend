package com.betterjr.dubbo.extend.filter.paracheck;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.dubbo.extend.filter.user.UserComsumerFilter;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Activate(group={Constants.PROVIDER})
public class ParaCheckByRuleFilter implements Filter{
	Logger logger=LoggerFactory.getLogger(UserComsumerFilter.class);

	private RuleServiceDubboFilterInvoker ruleServiceDubboFilterInvoker;

	public void setRuleServiceDubboFilterInvoker(RuleServiceDubboFilterInvoker ruleServiceDubboFilterInvoker) {
		this.ruleServiceDubboFilterInvoker = ruleServiceDubboFilterInvoker;
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		try{
			logger.debug("ParaCheckByRuleFilter test:invoker:"+this.ruleServiceDubboFilterInvoker);
			logger.debug("ParaCheckByRuleFilter test:doing paras checking.");
			return this.ruleServiceDubboFilterInvoker.doAround(invoker, invocation);
		}catch(Throwable e){
			e.printStackTrace();
			throw new RpcException(invocation.getMethodName()+"'s input parameters are valid.",e);
		}
	}

}
