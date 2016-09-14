package com.lubbo.common.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author benchu
 * @version on 15/10/24.
 * @since 0.1.0
 */
public interface ProxyFactory {
    /**
     * 生成代理对象
     *
     * @param clazz             被代理的class
     * @param invocationHandler
     *
     * @return 代理对象
     * if can't get the proxy of target calss.
     */
    <T> T proxy(Class<T> clazz, InvocationHandler invocationHandler);

    /**
     * 获取目标class的Caller对象
     *
     * @param clazz
     *
     * @return
     */
    DelegateCaller getDelegateCaller(Class<?> clazz);

    /**
     * 方法调用者
     *
     * @author mozhu
     */
    interface DelegateCaller {

        /**
         * call method.
         *
         * @param target     the actual object which runs the specified method.
         * @param methodName
         * @param args
         * @param argTypes
         *
         * @return the value returned by the method.
         */
        Object call(Object target, String methodName, Object[] args, Class<?>[] argTypes) throws Throwable;
    }

    interface MethodCaller {
        Object invoke(Object target, Object[] args) throws Throwable;
    }
}
