package com.lubbo.server;

import com.lubbo.common.proxy.ProxyFactory;
import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.Result;
import com.lubbo.core.exception.InvokeException;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public class ProxyInvokerFactory implements InvokerFactory<ExposeConfig> {
    private ProxyFactory deleteInvokerFactory;
    @Override
    public Invoker newInvoker(ExposeConfig config) {
        Class<?> serviceClass = config.getServiceClass();
        Object targetBean = config.getTargetBean();
        ProxyFactory.DelegateCaller delegateCaller= deleteInvokerFactory.getDelegateCaller(serviceClass);
        return new Invoker() {
            @Override
            public Result invoke(Invocation invocation) {
                Result result = new Result();
                try {
                    Object value= delegateCaller.call(targetBean, invocation.getMethodName(), invocation.getArguments(),
                                                 invocation.argTypes());
                    result.setValue(value);
                    result.setStatus(0);
                }catch (InvokeException e){
                    //框架内部异常 ,抛出
                  throw e;
                } catch (Throwable e) {
                    //执行异常.
                    //封装到 Result中
                    result.setException(e);
                    result.setStatus(1);
                }
                return result;
            }
        };
    }

    public void setDeleteInvokerFactory(ProxyFactory deleteInvokerFactory) {
        this.deleteInvokerFactory = deleteInvokerFactory;
    }
}
