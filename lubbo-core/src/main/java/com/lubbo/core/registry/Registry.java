package com.lubbo.core.registry;

import java.util.List;

/**
 * Created by benchu on 15/10/21.
 */
public interface Registry<T> {
    void createPersistent(String path);
    void createPersistentIfNeeded(String path);
    void createEphemeral(String path);
    void createEphemeralIfNeeded(String path);
    void delete(String path);
    List<String> getChildren(String path);
    void subscribe(String path,T t);
    boolean isConnected();
    void doClose();

}
