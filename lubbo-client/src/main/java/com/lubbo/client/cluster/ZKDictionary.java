package com.lubbo.client.cluster;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import com.alibaba.fastjson.JSONObject;
import com.lubbo.client.Dictionary;
import com.lubbo.client.provider.Provider;
import com.lubbo.client.provider.ProviderInvokerManager;
import com.lubbo.common.LubboConstants;
import com.lubbo.common.util.IPUtil;
import com.lubbo.common.util.ParamUtil;
import com.lubbo.core.registry.URLS;
import com.lubbo.core.registry.ZkRegistry;

/**
 * @author benchu
 * @version 16/9/19.
 */
public class ZKDictionary implements Dictionary {
    private ZkRegistry<PathChildrenCacheListener> zkRegistry;
    private Map<String, List<Provider>> serviceProviders = new ConcurrentHashMap<>();
    private ProviderInvokerManager providerInvokerManager;

    @Override
    public List<Provider> getProviders(String service) {
        return serviceProviders.get(service);
    }

    @Override
    public void initService(String service) {
        List<Provider> providers = new ArrayList<>();
        String servicePath = URLS.provider(service);
        List<String> children = zkRegistry.getChildren(servicePath);
        for (String hostPort : children) {
            byte[] data = zkRegistry.getData(servicePath + "/" + hostPort);
            Provider provider = buildProviderFromData(hostPort, data);
            if (provider != null) {
                addProvider(providers, provider);
            }
        }

        zkRegistry.subscribe(servicePath, (framework, event) -> {
            ChildData childData = event.getData();
            String hostPort = URLS.getHostFromPath(childData.getPath());
            Provider provider = buildProviderFromData(hostPort, childData.getData());
            switch (event.getType()) {
                case CHILD_ADDED:
                    addProvider(providers, provider);
                    break;
                case CHILD_REMOVED:
                    removeProvider(providers, provider);
                    break;
                case CHILD_UPDATED:
                    //todo.
                    break;
                default:
                    break;
            }
        });
        serviceProviders.put(service, providers);
    }

    public static Provider buildProviderFromData(String hostPort, byte[] bytes) {
        if (bytes != null) {
            String dataStr = new String(bytes, LubboConstants.DEFAULT_CHARSET);
            Map<String, Object> data = JSONObject.parseObject(dataStr);
            int weight = data != null ? ParamUtil.parseInt(data, "weight", 100) : 100;
            InetSocketAddress address = IPUtil.parseToSocketAddress(hostPort);
            return new Provider(address, weight);
        }
        return null;
    }

    public void addProvider(List<Provider> list, Provider provider) {
        providerInvokerManager.addProvider(provider);
        list.add(provider);
    }

    public void removeProvider(List<Provider> list, Provider provider) {
        providerInvokerManager.removeProvider(provider);
        list.remove(provider);
    }

    public void setZkRegistry(ZkRegistry zkRegistry) {
        this.zkRegistry = zkRegistry;
    }

    public void setProviderInvokerManager(ProviderInvokerManager providerInvokerManager) {
        this.providerInvokerManager = providerInvokerManager;
    }
}
