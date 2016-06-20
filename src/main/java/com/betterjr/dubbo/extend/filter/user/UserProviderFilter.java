package com.betterjr.dubbo.extend.filter.user;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.utils.StaticThreadLocal;

@Activate(group={Constants.PROVIDER})
public class UserProviderFilter implements Filter {
	Logger logger=LoggerFactory.getLogger(UserProviderFilter.class);
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		logger.debug("UserProviderFilter test :"+invocation.toString());
		logger.debug("UserProviderFilter test :"+invoker.getUrl());
		String user=invocation.getAttachment(FilterConstants.FilterAttachmentSessionId);
		logger.debug("UserProviderFilter test :sessionid="+user);
		logger.debug("UserProviderFilter test :invoker interface="+invoker.getInterface().getName());
		StaticThreadLocal.storeSessionId(user);
		return invoker.invoke(invocation);
	}

}
