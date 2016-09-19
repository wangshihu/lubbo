package com.lubbo.client.cluster;

import java.io.IOException;
import java.util.List;

import com.lubbo.client.Dictionary;
import com.lubbo.client.loadBalance.LoadBalance;
import com.lubbo.client.provider.Provider;
import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerLookUp;
import com.lubbo.core.Result;

/**
 * @author benchu
 * @version on 15/10/24.
 */
public class ClusterInvoker implements Invoker {

    private InvokerLookUp<Provider> invokerLookUp;
    private Dictionary dictionary;
    private LoadBalance loadBalance;

    @Override
    public Result invoke(Invocation invocation) {
        List<Provider> providers = dictionary.getProviders(invocation.getService());
        Provider provider = loadBalance.select(providers, invocation);
        Invoker invoker = invokerLookUp.lookUp(provider);
        return invoker.invoke(invocation);
    }

    @Override
    public void close() throws IOException {
    }
}
