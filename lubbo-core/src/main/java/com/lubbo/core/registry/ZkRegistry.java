package com.lubbo.core.registry;

import java.io.Closeable;
import java.util.List;

/**
 * @author benchu
 * @version 15/10/21.
 */
public interface ZkRegistry<T> extends Closeable {

    void createPersistentIfNeeded(String path);

    void createEphemeralIfNeeded(String path);

    void delete(String path);

    List<String> getChildren(String path);

    byte[] getData(String path);

    void subscribe(String path, T t);

    boolean isConnected();


}
