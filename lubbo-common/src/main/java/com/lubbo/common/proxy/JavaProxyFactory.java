package com.lubbo.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by benchu on 15/10/24.
 */
public class JavaProxyFactory extends AbstractProxyFactory {
    public JavaProxyFactory() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> clazz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, invocationHandler);
    }

    @Override
    protected MethodCaller getMethodCaller(Method method,Class<?>clazz) {
        return new MethodCaller() {
            @Override
            public Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException {
                return method.invoke(target,args);
            }
        };
    }
}
