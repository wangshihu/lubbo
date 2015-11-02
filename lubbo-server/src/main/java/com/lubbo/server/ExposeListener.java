package com.lubbo.server;

import java.io.Closeable;

import com.lubbo.core.Invoker;

/**
 * Created by benchu on 15/11/1.
 */
public interface ExposeListener extends Closeable {
    void expose(Invoker invoker, ExposeConfig exposeConfig);
    /**
     * 服务取消暴露监听
     *
     * @param exposeConfig
     */
    void unexposed(ExposeConfig exposeConfig);
}
