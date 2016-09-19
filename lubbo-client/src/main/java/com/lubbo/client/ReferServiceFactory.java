package com.lubbo.client;

import java.lang.reflect.InvocationHandler;

import org.springframework.beans.factory.FactoryBean;

import com.lubbo.client.cluster.InvokeInvocationHandler;
import com.lubbo.common.proxy.ProxyFactory;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;

/**
 * @author  benchu
 * @version on 15/10/24.
 */

public class ReferServiceFactory<T> implements FactoryBean<T> {

    private ProxyFactory proxyFactory;
    private InvokerFactory<ReferConfig> invokerFactory;
    private Class<T> targetClass;

    @Override
    public T getObject() throws Exception {
        return this.newInstance();
    }


    private T newInstance(){
        ReferConfig<T> referConfig = new ReferConfig<>(targetClass);
        Invoker invoker = invokerFactory.newInvoker(referConfig);
        InvocationHandler invocationHandler = new InvokeInvocationHandler(targetClass.getName(),invoker);
        return proxyFactory.proxy(targetClass,invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public InvokerFactory getInvokerFactory() {
        return invokerFactory;
    }

    public void setInvokerFactory(InvokerFactory invokerFactory) {
        this.invokerFactory = invokerFactory;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }
}
