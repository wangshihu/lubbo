package com.lubbo.core;

/**
 * Created by benchu on 15/11/1.
 */
public interface InvokerManager<K>  {
    Invoker getInvoker(K key);
}
