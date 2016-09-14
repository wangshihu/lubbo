package com.lubbo.core.registry;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author benchu
 * @version 16/9/14.
 */
public class AbstractZkTest {
    protected static ZooKeeperServerMain zkServer = null;
    protected static ExecutorService executorService = Executors.newCachedThreadPool();
    protected static final String TEST_ZK_PORT = "11111";

    @BeforeClass
    public static void beforeClass() throws IOException, KeeperException, InterruptedException {
        FileUtils.deleteDirectory(new File("/tmp/zk"));
        //
        zkServer = new ZooKeeperServerMain();
        ServerConfig config = new ServerConfig();
        config.parse(new String[] {TEST_ZK_PORT, "/tmp/zk"});
        executorService.execute(() -> {
            try {
                zkServer.runFromConfig(config);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(2000);
    }

    @AfterClass
    public static void afterClass() {
        executorService.shutdownNow();
    }
}
