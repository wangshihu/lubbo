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


/**
 * ProxyFactory的委托。
 *
 * @author benchu
 *
 */
public class DelegateProxyFactory implements ProxyFactory {

    private ProxyFactory proxyDelegate;

    private ProxyFactory callerDelegate;

    public DelegateProxyFactory() {
    }

    public DelegateProxyFactory(ProxyFactory proxyDelegate, ProxyFactory callerDelegate) {
        this.proxyDelegate = proxyDelegate;
        this.callerDelegate = callerDelegate;
    }

    @Override
    public <T> T proxy(Class<T> clazz, InvocationHandler invcationHandler) {
        return this.proxyDelegate.proxy(clazz, invcationHandler);
    }

    @Override
    public DelegateCaller getDelegateCaller(Class<?> clazz) {
        return this.callerDelegate.getDelegateCaller(clazz);
    }

    public void setProxyDelegate(ProxyFactory proxyDelegate) {
        this.proxyDelegate = proxyDelegate;
    }


    public void setCallerDelegate(ProxyFactory callerDelegate) {
        this.callerDelegate = callerDelegate;
    }


}
