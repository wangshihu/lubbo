package com.lubbo.server.listener;

import java.io.IOException;

import com.lubbo.core.Invoker;
import com.lubbo.core.registry.Registry;
import com.lubbo.core.registry.URLS;
import com.lubbo.server.ExposeConfig;
import com.lubbo.server.ExposeListener;

/**
 * Created by benchu on 15/11/1.
 */
public class RegistryListener implements ExposeListener {
    private Registry registry;
    @Override
    public void expose(Invoker invoker, ExposeConfig exposeConfig) {
        String serviceUrl = URLS.getServiceUrl(exposeConfig.getService());
        registry.createPersistentIfNeeded(serviceUrl+"/providers");
        registry.createPersistentIfNeeded(serviceUrl + "/consumers");
        //插入当前节点.
        String ipUrl = URLS.getIpUrl(serviceUrl, true,exposeConfig.getIp());
        String serverHost=ipUrl+":"+exposeConfig.getPort();
        registry.createEphemeral(serverHost);
    }

    @Override
    public void unexposed(ExposeConfig exposeConfig) {

    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void close() throws IOException {
        registry.doClose();
    }
}
