package com.lubbo.core;

/**
 * @author benchu
 * @version 16/9/18.
 */
public interface InvokerLookUp<T> {
    Invoker lookUp(T key);
}
