package com.betterjr.dubbo.extend.filter.user;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.security.shiro.session.RedisSessionDAO;
import com.betterjr.common.utils.StaticThreadLocal;

@Activate(group={Constants.PROVIDER})
public class UserProviderFilter implements Filter {
	Logger logger=LoggerFactory.getLogger(UserProviderFilter.class);
	
	private RedisSessionDAO redisSessionDAO;
	
	public void setRedisSessionDAO(RedisSessionDAO redisSessionDAO) {
		this.redisSessionDAO = redisSessionDAO;
	}

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		logger.debug("UserProviderFilter test :"+invocation.toString());
		logger.debug("UserProviderFilter test :"+invoker.getUrl());
		String sessionId=invocation.getAttachment(FilterConstants.FilterAttachmentSessionId);
		if(sessionId!=null){
			logger.debug("UserProviderFilter test :sessionid="+sessionId);
			logger.debug("UserProviderFilter test :invoker interface="+invoker.getInterface().getName());
			StaticThreadLocal.storeSessionId(sessionId);
			
			Session session=redisSessionDAO.doReadSession(sessionId);
			if(session !=null){
				for(Object obj:session.getAttributeKeys()){
					logger.debug("UserProviderFilter test ,session attrs:"+session.getAttribute(obj));
				}
				StaticThreadLocal.storeSession(session);
				
				SimplePrincipalCollection col=(SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(col!=null){
					StaticThreadLocal.storePrincipal(col.getPrimaryPrincipal());
				}
			}
		}
		return invoker.invoke(invocation);
	}

}
