package com.lubbo.client.cluster;

import java.util.List;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import com.lubbo.client.ReferConfig;
import com.lubbo.common.LubboConstants;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.registry.URLS;
import com.lubbo.core.registry.ZkRegistry;

/**
 * @author benchu
 * @version on 15/10/24.
 */
public class ClusterInvokerFactory implements InvokerFactory<ReferConfig> {

    private ZkRegistry<PathChildrenCacheListener> registry;

    @Override
    public Invoker newInvoker(ReferConfig referConfig) {
        String serviceUrl = URLS.serviceUrl(referConfig.getTargetClass().getName());
        String ipUrl = URLS.consumerIp(serviceUrl, LubboConstants.LOCAL_HOST);
        registry.createEphemeralIfNeeded(ipUrl);
        String listenerPath = serviceUrl + LubboConstants.ZK_SEPARATOR + "providers";
        List<String> providers = registry.getChildren(listenerPath);
        ClusterInvoker invoker = new ClusterInvoker(providers);
        registry.subscribe(listenerPath, (client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    invoker.getCluster().addProvider(event.getData());
                    break;
                case CHILD_REMOVED:
                    invoker.getCluster().removeProvider(event.getData());
                    break;
                case CHILD_UPDATED:
                    invoker.getCluster().updateProvider(event.getData());
                    break;
                default:
                    break;
            }
        });
        return invoker;
    }

    public ZkRegistry<PathChildrenCacheListener> getRegistry() {
        return registry;
    }

    public void setRegistry(ZkRegistry registry) {
        this.registry = registry;
    }
}
