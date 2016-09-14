package com.lubbo.server.listener;

import java.io.IOException;
import java.util.List;

import com.lubbo.core.Invoker;
import com.lubbo.server.ExposeConfig;
import com.lubbo.server.ExposeListener;

/**
 * 聚合exposerListener
 * @author  benchu
 * @version on 15/11/1.
 */
public class MultiExposerListener implements ExposeListener {
    List<ExposeListener> exposeListeners;
    @Override
    public void expose(Invoker invoker, ExposeConfig exposeConfig) {
        exposeListeners.forEach(k->k.expose(invoker,exposeConfig));
    }

    @Override
    public void unexposed(ExposeConfig exposeConfig) {
        exposeListeners.forEach(k->k.unexposed(exposeConfig));
    }

    @Override
    public void close() throws IOException {
        for(ExposeListener listener:exposeListeners){
            listener.close();
        }
    }

    public void setExposeListeners(List<ExposeListener> exposeListeners) {
        this.exposeListeners = exposeListeners;
    }
}
