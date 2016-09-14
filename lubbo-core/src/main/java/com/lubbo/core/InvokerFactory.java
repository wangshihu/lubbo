package com.lubbo.core;

/**
 *
 * @author  benchu
 * @version on 15/10/24.
 */
public interface InvokerFactory<T> {
    Invoker newInvoker(T config);
}
