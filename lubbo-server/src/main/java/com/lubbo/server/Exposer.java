package com.lubbo.server;

import java.io.Closeable;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public interface Exposer extends Closeable {

    void expose();

    void unExpose();

    ExposeConfig getConfig();

    void setExposeListener(ExposeListener listener);

}
