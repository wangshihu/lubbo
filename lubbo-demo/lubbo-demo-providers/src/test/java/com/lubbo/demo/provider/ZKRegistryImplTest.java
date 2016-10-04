package com.lubbo.demo.provider;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import com.lubbo.common.LubboConstants;
import com.lubbo.core.registry.ZKRegistryImpl;

/**
 * @author benchu
 * @version on 15/10/31.
 */
public class ZKRegistryImplTest {
    ZKRegistryImpl registry;

    @org.junit.Before
    public void before() {
        registry = new ZKRegistryImpl("localhost:2181");
    }

    @Test
    public void test() {
        List<String> list = registry.getChildren("/lubbo/com.lubbo.demo.api.IdRange/providers");
        System.out.println(list);
        registry.createEphemeralIfNeeded("/test/test1");
        String s = new String(registry.getData("/test/test1"), LubboConstants.DEFAULT_CHARSET);
        System.out.println(s);
    }

    @Test
    public void test2() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                                      .connectString("localhost:2181")
                                      .sessionTimeoutMs(5000)
                                      .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                      .defaultData(null)
                                      .build();
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/test/test3");
        String s = new String(client.getData().forPath("/test/test3"),LubboConstants.DEFAULT_CHARSET);
        System.out.println(s);
    }

}
