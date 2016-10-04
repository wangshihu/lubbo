package com.lubbo.core.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

/**
 * @author benchu
 * @version on 15/10/21.
 */
public class ZKRegistryImpl implements ZkRegistry<PathChildrenCacheListener> {
    private CuratorFramework client;
    private final Map<String, PathChildrenCache> pathChildrenCacheMap = new ConcurrentHashMap<>();

    private String host;

    public ZKRegistryImpl(String host) {
        this.host = host;
        client = CuratorFrameworkFactory.builder()
                     .connectString(host)
                     .sessionTimeoutMs(5000)
                     .defaultData(null)
                     .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                     .build();
        client.getConnectionStateListenable().addListener((curatorFramework, state) -> {
            //TODO listener
        });
        client.start();
    }

    public void createPersistentIfNeeded(String path) {
        try {
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createEphemeralIfNeeded(String path) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (KeeperException.NoNodeException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    public void close() {
        client.close();
    }

    public void subscribe(String path, PathChildrenCacheListener listener) {
        try {
            PathChildrenCache cache = pathChildrenCacheMap.get(path);
            if (cache == null) {
                synchronized(pathChildrenCacheMap) {
                    cache = pathChildrenCacheMap.get(path);
                    if (cache == null) {
                        cache = new PathChildrenCache(client, path, true);
                        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                        pathChildrenCacheMap.put(path, cache);
                    }
                }
            }
            cache.getListenable().addListener(listener);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
