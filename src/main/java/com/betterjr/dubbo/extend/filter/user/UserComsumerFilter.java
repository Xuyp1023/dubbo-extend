package com.betterjr.dubbo.extend.filter.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.utils.UserUtils;

@Activate(group = { Constants.CONSUMER })
public class UserComsumerFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(UserComsumerFilter.class);

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		try {
			logger.debug("UserComsumerFilter test :" + invocation.toString());
			logger.debug("UserComsumerFilter test :" + invoker.getUrl());
			Session session = UserUtils.getSession();
			if (session != null) {
				String sessionid = (String) session.getId();
				logger.debug("UserComsumerFilter test :sessionid=" + sessionid);
				invocation.getAttachments().put(FilterConstants.FilterAttachmentSessionId, sessionid);
				logger.debug("UserComsumerFilter test :" + invocation.toString());
				logger.debug("UserComsumerFilter test invoker interface:" + invoker.getInterface().getName());
			}

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
			throw new RpcException(RpcException.BIZ_EXCEPTION,"no session id checked in "+this.getClass(),ex);
		}
		return invoker.invoke(invocation);
	}

}
