package com.lubbo.server.listener;

import java.io.IOException;

import com.lubbo.core.Invoker;
import com.lubbo.core.network.Server;
import com.lubbo.server.ExposeConfig;
import com.lubbo.server.ExposeListener;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public class NetworkExposeListener implements ExposeListener{
    private Server server;
    @Override
    public void expose(Invoker invoker, ExposeConfig exposeConfig) {
        server.bind(exposeConfig.getPort());
    }

    @Override
    public void unexposed(ExposeConfig exposeConfig) {

    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void close() throws IOException {

    }
}
