package com.lubbo.client.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lubbo.client.ProviderInvokerManager;
import com.lubbo.common.util.IOUtils;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerFactory;
import com.lubbo.core.InvokerLookUp;

/**
 * @author benchu
 * @version 16/9/18.
 */
public class ProviderInvokerLookUp implements InvokerLookUp<Provider>, ProviderInvokerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderInvokerLookUp.class);
    private final ConcurrentMap<Provider, Invoker> providerInvokers = new ConcurrentHashMap<>();
    private InvokerFactory<Provider> invokerFactory;

    @Override
    public Invoker lookUp(Provider provider) {
        return providerInvokers.get(provider);
    }

    @Override
    public synchronized void addProvider(Provider provider) {
        if (!providerInvokers.containsKey(provider)) {
            Invoker invoker = invokerFactory.newInvoker(provider);
            providerInvokers.put(provider, invoker);
        }
    }

    @Override
    public synchronized void removeProvider(Provider provider) {
        Invoker invoker = providerInvokers.remove(provider);
        IOUtils.closeQuitely(invoker);
    }

    public void setInvokerFactory(InvokerFactory<Provider> invokerFactory) {
        this.invokerFactory = invokerFactory;
    }
}
