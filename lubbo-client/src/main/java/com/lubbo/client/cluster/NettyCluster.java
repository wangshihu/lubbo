package com.lubbo.client.cluster;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lubbo.client.network.NettyClient;
import com.lubbo.client.provider.Provider;
import com.lubbo.core.network.Client;
import com.lubbo.core.network.netty.NettyClient;

/**
 * @author  benchu
 * @version on 15/10/25.
 */
public class NettyCluster implements Cluster{
    private static Logger logger = LoggerFactory.getLogger(NettyCluster.class);
    private List<String> providers ;
    private Map<String, Client> nettyClientMap = new ConcurrentHashMap<>();
    int index = 0;

    public NettyCluster(List<String> providers) {
        Preconditions.checkArgument(providers!=null&&!providers.isEmpty(),"cannot find provider service host");
        this.providers = Lists.newArrayList(providers);
        ImmutableList.copyOf(providers).forEach(this::addProvider);
    }
    @Override
    public ProviderInvoker getProviderInvoker() {
        return null;
    }

    @Override
    public void updateProvider(Provider provider) {

    }

    @Override
    public void removeProvider(Provider provider) {

    }

    @Override
    public void addProvider(Provider provider) {

    }

    /**
     * 路由算法.目前只是简单的循环.
     * @return
     */
    public NettyClient getNettyClient() {
        index++;
        if(index==providers.size()){
            index=0;
        }
        return nettyClientMap.get(providers.get(0));
    }

    //fixme 这里是data.getPath是全路径
    public void updateProvider(ChildData data) {

    }

    public void removeProvider(ChildData data) {
        String providerHost = getProviderHost(data);
        if (!providers.contains(providerHost) && nettyClientMap.get(providerHost) == null) {
            logger.warn("provider has been unRegistry");
            return;
        }
        providers.remove(providerHost);
        NettyClient nettyClient = nettyClientMap.remove(providerHost);
        if(nettyClient!=null)
            nettyClient.stop();
    }

    public void addProvider(String providerHost) {
        providers.add(providerHost);
        NettyClient nettyClient = new NettyClient(providerHost);
        nettyClient.start();
        nettyClientMap.put(providerHost, nettyClient);
    }

    public void addProvider(ChildData data) {
        String providerHost = getProviderHost(data);
        if (providers.contains(providerHost) || nettyClientMap.get(providerHost) != null) {
            logger.warn("provider:"+providerHost+" has been registry");
            return;
        }
        addProvider(providerHost);
    }

    private String getProviderHost(ChildData data) {
        String path = data.getPath();
        int index = path.lastIndexOf("/");
        return path.substring(index+1, path.length());
    }


}
