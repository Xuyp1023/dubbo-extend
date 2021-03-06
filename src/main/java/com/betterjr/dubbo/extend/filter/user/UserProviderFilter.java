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
import com.betterjr.common.utils.UserUtils;

@Activate(group = { Constants.PROVIDER }, order = 40000)
public class UserProviderFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(UserProviderFilter.class);

    @Override
    public Result invoke(final Invoker<?> invoker, final Invocation invocation) throws RpcException {
        UserUtils.clearThreadLocal();
        final String method = invocation.getMethodName();
        if (!method.startsWith(FilterConstants.filterUserFilterMethodPrefix)) {
            try {
                logger.debug("UserProviderFilter test :" + invocation.toString());
                logger.debug("UserProviderFilter test :" + invoker.getUrl());
                final String sessionId = invocation.getAttachment(FilterConstants.FilterAttachmentSessionId);
                if (sessionId != null) {
                    logger.debug("UserProviderFilter test :sessionid=" + sessionId);
                    logger.debug("UserProviderFilter test :invoker interface=" + invoker.getInterface().getName());
                    UserUtils.storeSessionId(sessionId);
                }
                UserUtils.storeRequestIp(invocation.getAttachment(FilterConstants.FilterAttachmentRequestAddress));
            }
            catch (final Exception ex) {
                logger.error(ex.getLocalizedMessage(), ex);
                throw new RpcException(RpcException.BIZ_EXCEPTION, "no sessiondId checked in " + this.getClass(), ex);
            }
        }
        return invoker.invoke(invocation);
    }

}
