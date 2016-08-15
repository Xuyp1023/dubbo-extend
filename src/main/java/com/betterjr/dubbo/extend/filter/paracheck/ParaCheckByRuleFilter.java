package com.betterjr.dubbo.extend.filter.paracheck;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.utils.StaticThreadLocal;
import com.betterjr.dubbo.extend.filter.user.UserComsumerFilter;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Activate(group={Constants.PROVIDER},order = 40000)
public class ParaCheckByRuleFilter implements Filter{
	Logger logger=LoggerFactory.getLogger(UserComsumerFilter.class);

	private RuleServiceDubboFilterInvoker ruleServiceDubboFilterInvoker;

	public void setRuleServiceDubboFilterInvoker(RuleServiceDubboFilterInvoker ruleServiceDubboFilterInvoker) {
		this.ruleServiceDubboFilterInvoker = ruleServiceDubboFilterInvoker;
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		try{
			//将入参写入缓存
			this.initMethodPara(invoker, invocation);
			
			//参数验证
			logger.debug("ParaCheckByRuleFilter test:invoker:"+this.ruleServiceDubboFilterInvoker);
			logger.debug("ParaCheckByRuleFilter test:doing paras checking.");
			Result re= this.ruleServiceDubboFilterInvoker.doAround(invoker, invocation);
			
			return re;
		}catch(BytterException byEx){
		    logger.error(byEx.getMessage(),byEx);
		    throw byEx;
		}
		catch(RpcException ex){
			logger.error(ex.getMessage(),ex);
			throw ex;
		}
		catch(Throwable e){
			logger.error(e.getMessage(),e);
			throw new RpcException(e.getMessage(),e);
		}
	}
	
	public void initMethodPara(Invoker<?> invoker, Invocation invocation){
		Class cls=invoker.getInterface();
		Object[] arguValues=invocation.getArguments();
		String methName=invocation.getMethodName();
		try {
			Method method=cls.getMethod(methName, invocation.getParameterTypes());
			Map map=new HashMap();
			int index=0;
			for(Parameter para:method.getParameters()){
				map.put(para.getName(), arguValues[index]);
				index++;
			}
			StaticThreadLocal.storeDubboMethodParaMap(map);
			logger.debug("ParaCheckByRuleFilter test:write method para map:"+map);
		} catch (Exception e) {
			logger.error("init Method paras failed! ",e);
		} 
	}

}
