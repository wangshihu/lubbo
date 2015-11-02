package com.lubbo.common.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.lubbo.common.util.ClassUtil;

/**
 * Created by benchu on 15/10/24.
 */
public abstract class AbstractProxyFactory implements ProxyFactory {
    private Map<Class, DelegateCaller> delegateCallerMap = new HashMap<>();

    /**
     * 解析clazz的所有方法,映射到map中.
     * @param clazz
     * @return
     */
    @Override
    public DelegateCaller getDelegateCaller(Class clazz) {
        DelegateCaller delegateCaller = delegateCallerMap.get(clazz);
        if (delegateCaller == null) {
            Map<String, Map<Collection<Class<?>>, MethodCaller>> overrideMethodCallers = new HashMap<>();
            List<Method> methods = getMethodsCall(clazz);
            for (Method method : methods) {
                Map<Collection<Class<?>>, MethodCaller> methodCallers = overrideMethodCallers.get(method.getName());
                if (methodCallers == null) {
                    methodCallers = new HashMap<>();
                    overrideMethodCallers.put(method.getName(), methodCallers);
                }
                Collection<Class<?>> args = Arrays.asList(method.getParameterTypes());
                MethodCaller methodcaller = this.getMethodCaller(method,clazz);
                methodCallers.put(args, methodcaller);
            }
            delegateCaller = new DefaultDelegateCaller(overrideMethodCallers);
            delegateCallerMap.put(clazz,delegateCaller);
        }
        return delegateCaller;
    }
    protected List<Method> getMethodsCall(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> methodsCall = new ArrayList<>();
        for (Method method : methods) {
            // 过滤静态方法及Object的final方法
            if (method.getDeclaringClass().equals(Object.class) && Modifier.isFinal(method.getModifiers()) ||
                    Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            methodsCall.add(method);
        }
        return methodsCall;
    }

    protected static class DefaultDelegateCaller implements DelegateCaller {
        private Map<String, Map<Collection<Class<?>>, MethodCaller>> overrideMethodCallers;

        public DefaultDelegateCaller(Map<String, Map<Collection<Class<?>>, MethodCaller>> overrideMethodCallers) {
            this.overrideMethodCallers = overrideMethodCallers;
        }

        @Override
        public Object call(Object target, String methodName, Object[] args, Class<?>[] argTypes) throws Throwable {
            Map<Collection<Class<?>>, MethodCaller> methodCallers = overrideMethodCallers.get(methodName);
            Preconditions.checkNotNull(methodCallers, "cannot find methodCallers by method =" + methodName);
            MethodCaller methodCaller = methodCallers.get(Arrays.asList(argTypes));
            Preconditions.checkNotNull(methodCaller,
                                          "cannot find methodCaller by argTypes =" + ClassUtil.arrayToString(argTypes));
            return methodCaller.invoke(target, args);
        }
    }

    interface MethodCaller {
        Object invoke(Object target, Object[] args) throws Throwable;
    }

    /**
     * get {@link MethodCaller} of the specified method and class.
     *
     * @param method
     *            method
     * @param clazz
     *            the class that will invoke the method.
     * @return
     */
    protected abstract MethodCaller getMethodCaller(Method method, Class<?> clazz);
}
