package com.lubbo.server.listener;

import java.io.IOException;

import com.lubbo.common.LubboConstants;
import com.lubbo.core.Invoker;
import com.lubbo.core.registry.URLS;
import com.lubbo.core.registry.ZkRegistry;
import com.lubbo.server.ExposeConfig;
import com.lubbo.server.ExposeListener;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public class RegistryListener implements ExposeListener {
    private ZkRegistry registry;

    @Override
    public void expose(Invoker invoker, ExposeConfig exposeConfig) {
        String serviceUrl = URLS.serviceUrl(exposeConfig.getService());
        registry.createPersistentIfNeeded(serviceUrl + "/providers");
        registry.createPersistentIfNeeded(serviceUrl + "/consumers");
        //插入当前节点.
        String ipUrl = URLS.providerIp(serviceUrl, LubboConstants.LOCAL_HOST);
        String serverHost = ipUrl + ":" + LubboConstants.LUBBO_PORT;
        registry.createEphemeral(serverHost);
    }

    @Override
    public void unexposed(ExposeConfig exposeConfig) {

    }

    public void setRegistry(ZkRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void close() throws IOException {
        registry.doClose();
    }
}
