package com.lubbo.core.network;

import java.io.Closeable;

/**
 * Channel
 *
 */
public interface Channel extends Closeable {

    Integer getId();

    void write(Object obj);

    void flush();

    void writeAndFlush(Object obj);

    boolean isAvailable();
}
