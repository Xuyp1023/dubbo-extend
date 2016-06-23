package com.betterjr.dubbo.extend.filter.user;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;


@Activate(group={Constants.CONSUMER})
public class UserComsumerFilter implements Filter{
	Logger logger=LoggerFactory.getLogger(UserComsumerFilter.class);
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		Subject subject = SecurityUtils.getSubject();  
		String sessionid=(String) subject.getSession().getId();
		logger.debug("UserComsumerFilter test :"+invocation.toString());
		logger.debug("UserComsumerFilter test :"+invoker.getUrl());
		logger.debug("UserComsumerFilter test :sessionid="+sessionid);
		invocation.getAttachments().put(FilterConstants.FilterAttachmentSessionId, sessionid);
		logger.debug("UserComsumerFilter test :"+invocation.toString());
		logger.debug("UserComsumerFilter test invoker interface:"+invoker.getInterface().getName());
		return invoker.invoke(invocation);
	}

}
