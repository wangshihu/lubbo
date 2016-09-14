/*
 * Copyright 2011-2014 Mogujie Co.Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lubbo.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;



/**
 * 使用Cglib实现的ProxyFactory
 *
 * @author benchu
 *
 */
public class CglibProxyFactory extends AbstractProxyFactory implements ProxyFactory {

    private Map<Class<?>, FastClass> fastClassCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> clazz,  InvocationHandler invcationHandler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 此处在Callback的子类中之所以选择了MethodInterceptor，而不选择InvocationHandler是因为
        // 1、MethodInterceptor扩展性更好;
        // 2、InvocationHandler与JDK的类重名，书写麻烦.
        enhancer.setCallback(new MethodInterceptorAdaptor(invcationHandler));

        return (T) enhancer.create();
    }



    /**
     * MethodInterceptor的适配器，将{@link InvocationHandler}适配为
     * {@link MethodInterceptor}
     *
     * @author mozhu
     *
     */
    private static class MethodInterceptorAdaptor implements MethodInterceptor {

        private InvocationHandler invocationHandler;

        private MethodInterceptorAdaptor(InvocationHandler invocationHandler) {
            this.invocationHandler = invocationHandler;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return invocationHandler.invoke(obj, method, args);
        }

    }

    @Override
    protected MethodCaller getMethodCaller(Method method, Class<?> clazz) {
        FastClass fastClass = this.fastClassCache.get(clazz);
        if (fastClass == null) {
            fastClass = FastClass.create(clazz);
            fastClassCache.put(clazz, fastClass);
        }
        FastMethod fastMethod = fastClass.getMethod(method);
        return new MethodCallerAdaptor(fastMethod);
    }

    private static class MethodCallerAdaptor implements MethodCaller {

        private final FastMethod fastMethod;

        public MethodCallerAdaptor(FastMethod fastMethod) {
            this.fastMethod = fastMethod;
        }

        @Override
        public Object invoke(Object targetBean, Object[] args) throws Throwable {
            try {
                return fastMethod.invoke(targetBean, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

    }
}
