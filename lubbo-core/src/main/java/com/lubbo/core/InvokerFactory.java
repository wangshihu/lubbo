package com.lubbo.core;

/**
 *
 * Created by benchu on 15/10/24.
 */
public interface InvokerFactory<T> {
    Invoker newInvoker(T config);
}
