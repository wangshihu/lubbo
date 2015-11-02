package com.lubbo.client.cluster;

/**
 * provider的抽象集群.
 * Created by benchu on 15/11/1.
 */
public interface Cluster {
    ProviderInvoker getProviderInvoker();
    void updateProvider(Provider provider);
    void removeProvider(Provider provider);
    void addProvider(Provider provider);
}
