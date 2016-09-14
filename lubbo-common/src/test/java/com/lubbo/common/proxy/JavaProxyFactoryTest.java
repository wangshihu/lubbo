package com.lubbo.common.proxy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.lubbo.common.proxy.AbstractProxyFactory.DefaultDelegateCaller;

/**
 * @author benchu
 * @version 16/9/13.
 */
public class JavaProxyFactoryTest {
    private ProxyFactory proxyFactory = new JavaProxyFactory();

    @Test
    public void testGetDelegateCaller() {
        DefaultDelegateCaller delegateCaller = (DefaultDelegateCaller) proxyFactory.getDelegateCaller(ProxyEntry.class);
        Map<String, Map<List<Class<?>>, ProxyFactory.MethodCaller>> map = delegateCaller.getOverrideMethodCallers();
        Assert.assertEquals(map.size(),2);
        Map<List<Class<?>>, ProxyFactory.MethodCaller>funcMap =  map.get("func");
        Assert.assertEquals(funcMap.size(),2);
        Assert.assertNotNull(funcMap.get(Lists.newArrayList(TestObj.class)));
        Assert.assertNotNull(funcMap.get(Lists.newArrayList(int.class)));
    }

    @Test
    public void test() throws Throwable {
        ProxyFactory.DelegateCaller delegateCaller = proxyFactory.getDelegateCaller(ProxyEntry.class);
        AtomicInteger callCounter = new AtomicInteger();
        ProxyEntry targetBean = new ProxyEntry() {
            @Override
            public int func(int c) {
                callCounter.addAndGet(1);
                return c+10;
            }

            @Override
            public void func(TestObj obj) {
                callCounter.addAndGet(1);
                obj.setName("hello");
            }

            @Override
            public void func2() {
                callCounter.addAndGet(1);
            }
        };
        TestObj param = new TestObj();
        delegateCaller.call(targetBean, "func2", new Object[]{}, new Class[]{});
        delegateCaller.call(targetBean, "func", new Object[]{param}, new Class[]{TestObj.class});
        Integer c = (Integer) delegateCaller.call(targetBean, "func", new Object[]{1}, new Class[]{int.class});
        Assert.assertEquals(c.intValue(),11);
        Assert.assertEquals(callCounter.get(),3);

    }
}
