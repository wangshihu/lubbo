package com.lubbo.server.listener;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerManager;
import com.lubbo.server.ExposeConfig;
import com.lubbo.server.ExposeListener;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public class InvokerExposerManager implements InvokerManager<Invocation> ,ExposeListener {
    private Map<String,Invoker> invokerMap = new ConcurrentHashMap<>();
    @Override
    public void expose(Invoker invoker, ExposeConfig exposeConfig) {
        invokerMap.put(exposeConfig.getService(),invoker);
    }

    @Override
    public void unexposed(ExposeConfig exposeConfig) {

    }

    @Override
    public void close() throws IOException {
        invokerMap.clear();
    }

    @Override
    public Invoker getInvoker(Invocation invocation) {
        return invokerMap.get(invocation.getService());
    }
}
