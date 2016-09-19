package com.lubbo.client.cluster;

import com.lubbo.client.provider.Provider;

/**
 * provider的抽象集群.
 * @author  benchu
 * @version on 15/11/1.
 */
public interface Cluster {
    ProviderInvoker getProviderInvoker();

    void updateProvider(Provider provider);

    void removeProvider(Provider provider);

    void addProvider(Provider provider);
}
