package com.lubbo.core;

import java.io.Closeable;

/**
 * @author  benchu
 * @version on 15/10/24.
 */
public interface Invoker extends Closeable{
    Result invoke(Invocation invocation);
}
