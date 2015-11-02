package com.lubbo.client.network;

import java.util.List;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import com.lubbo.client.ReferConfig;
import com.lubbo.common.Constants;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.registry.Registry;
import com.lubbo.core.registry.URLS;

/**
 * Created by benchu on 15/10/24.
 */
public class ClientInvokerFactory implements InvokerFactory<ReferConfig> {
    //FIXME 客户端的ip
    private String ip="127.0.0.1";

    private Registry registry;

    @Override
    public Invoker newInvoker(ReferConfig referConfig) {
        String serviceUrl = URLS.getServiceUrl(referConfig.getService());
        String ipUrl = URLS.getIpUrl(serviceUrl,false,ip);
        registry.createEphemeralIfNeeded(ipUrl);
        String listenerPath = serviceUrl+ Constants.ZK_SEPARATOR+"providers";
        List<String> providers = registry.getChildren(listenerPath);
        ClientInvoker invoker = new ClientInvoker(providers);
        registry.subscribe(listenerPath, (PathChildrenCacheListener) (client, event) -> {
            switch (event.getType()){
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

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
