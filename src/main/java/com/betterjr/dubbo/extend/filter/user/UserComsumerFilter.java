package com.betterjr.dubbo.extend.filter.user;

import java.lang.reflect.Method;

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
import com.betterjr.common.annotation.NoSession;
import com.betterjr.common.utils.UserUtils;

@Activate(group = { Constants.CONSUMER },order = 40000)
public class UserComsumerFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(UserComsumerFilter.class);

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
	    String method=invocation.getMethodName();
	    
	    if(!method.startsWith(FilterConstants.filterUserFilterMethodPrefix)){
	        boolean noSession=false;
	        try {
                Method me=invoker.getInterface().getMethod(method, invocation.getParameterTypes());
                if(me.isAnnotationPresent(NoSession.class)){
                    noSession=true;
                }
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            
	        if(!noSession){
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
	        }
	    }
		return invoker.invoke(invocation);
	}

}
