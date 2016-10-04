package com.lubbo.client.cluster;

import com.lubbo.client.Dictionary;
import com.lubbo.client.ReferConfig;
import com.lubbo.client.loadBalance.LoadBalance;
import com.lubbo.client.provider.Provider;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.InvokerLookUp;

/**
 * @author benchu
 * @version on 15/10/24.
 */
public class ClusterInvokerFactory implements InvokerFactory<ReferConfig> {

    private InvokerLookUp<Provider> invokerLookUp;

    private Dictionary dictionary;

    private LoadBalance loadBalance;

    @Override
    public Invoker newInvoker(ReferConfig config) {
        ClusterInvoker invoker = new ClusterInvoker();
        dictionary.initService(config.getTargetClass().getName());
        invoker.setDictionary(dictionary);
        invoker.setLoadBalance(loadBalance);
        invoker.setInvokerLookUp(invokerLookUp);
        return invoker;
    }

    public void setInvokerLookUp(InvokerLookUp<Provider> invokerLookUp) {
        this.invokerLookUp = invokerLookUp;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }
}
