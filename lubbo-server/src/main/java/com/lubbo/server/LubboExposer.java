package com.lubbo.server;

import java.io.IOException;

import com.lubbo.core.Invoker;

/**
 * Created by benchu on 15/11/1.
 */
public class LubboExposer implements Exposer {
    private ExposeListener exposeListener;
    private ExposeConfig exposeConfig;
    private Invoker invoker;

    public LubboExposer(ExposeListener exposeListener, ExposeConfig exposeConfig, Invoker invoker) {
        this.exposeListener = exposeListener;
        this.exposeConfig = exposeConfig;
        this.invoker = invoker;
    }

    @Override
    public void expose() {
        exposeListener.expose(invoker,exposeConfig);
    }
    //TODO 取消暴露服务.
    @Override
    public void unExpose() {

    }

    @Override
    public ExposeConfig getConfig() {
        return exposeConfig;
    }

    @Override
    public void setExposeListener(ExposeListener exposeListener) {
        this.exposeListener = exposeListener;
    }

    public void setExposeConfig(ExposeConfig exposeConfig) {
        this.exposeConfig = exposeConfig;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void close() throws IOException {
        exposeListener.close();
    }
}
