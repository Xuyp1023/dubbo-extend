
package com.betterjr.dubbo.extend.filter.execption;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.betterjr.common.exception.BytterException;


@Activate(group = Constants.PROVIDER,order = 30000)
public class ThrowBytterExceptionFilter implements Filter {

    private final Logger logger;
    
    public ThrowBytterExceptionFilter() {
        this(LoggerFactory.getLogger(ThrowBytterExceptionFilter.class));
    }
    
    public ThrowBytterExceptionFilter(Logger logger) {
        this.logger = logger;
    }
    
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = result.getException();

                    // 如果是bytterException异常，直接抛出
                    if (exception instanceof BytterException) {
                        return new RpcResult(new RpcException(exception));
                    }
                } catch (Throwable e) {
                    logger.warn("Fail to ThrowBytterExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
                    return result;
                }
            }
            return result;
        } catch (RuntimeException e) {
            // 如果是bytterException异常，直接抛出
            if (e instanceof BytterException) {
                return new RpcResult(new RpcException(e));
            }else{
                logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
                        + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                        + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
                throw e;
            }
        }
    }

}