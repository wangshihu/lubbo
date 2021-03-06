package com.lubbo.core.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author benchu
 * @since 0.1
 */
public class RegistryTest extends AbstractZkTest {
    ZKRegistryImpl registry;

    @Before
    public void before() {
        registry = new ZKRegistryImpl("localhost:" + TEST_ZK_PORT);
    }

    @Test
    public void testEphemeral() {
        registry.createEphemeralIfNeeded("/lubbo/com.HelloWorld/providers/192.160:80");
        registry.createEphemeralIfNeeded("/lubbo/com.HelloWorld/providers/192.160:81");
        registry.createEphemeralIfNeeded("/lubbo/com.HelloWorld/providers/192.160:82");
        List<String> list = registry.getChildren("/lubbo/com.HelloWorld/providers");
        Assert.assertTrue(list.contains("192.160:80"));
        Assert.assertTrue(list.contains("192.160:81"));
        Assert.assertTrue(list.contains("192.160:82"));
    }

    @Test
    public void testListener() throws InterruptedException {
        List<String> providers = new ArrayList<>();
        registry.subscribe("/lubbo/com.HelloWorld/providers", new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String path = event.getData().getPath();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        providers.add(path);
                        break;
                    case CHILD_REMOVED:
                        providers.remove(path);
                        break;
                    case CHILD_UPDATED:
                        break;
                    default:
                        break;
                }
            }
        });
        registry.createEphemeralIfNeeded("/lubbo/com.HelloWorld/providers/192.160:80");
        Thread.sleep(500);
        Assert.assertTrue(providers.contains("/lubbo/com.HelloWorld/providers/192.160:80"));
        registry.createEphemeralIfNeeded("/lubbo/com.HelloWorld/providers/192.160:81");
        Thread.sleep(500);
        Assert.assertTrue(providers.contains("/lubbo/com.HelloWorld/providers/192.160:81"));
        registry.delete("/lubbo/com.HelloWorld/providers/192.160:81");
        Thread.sleep(500);
        Assert.assertFalse(providers.contains("/lubbo/com.HelloWorld/providers/192.160:81"));
    }

    @Test
    public void test(){
        ZKRegistryImpl registry = new ZKRegistryImpl("10.13.20.13:2181");
        System.out.println(registry.getChildren("/lubbo/com.lubbo.demo.api.IdRange/providers"));
    }

}
