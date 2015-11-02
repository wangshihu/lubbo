package com.lubbo.core.network;

import java.io.Closeable;

/**
 * Channel
 *
 * @author mozhu
 *
 */
public interface Channel extends Closeable {

    Integer getId();

    void write(Object obj);

    void flush();

    void writeAndFlush(Object obj);

    boolean isAvaiable();
}
