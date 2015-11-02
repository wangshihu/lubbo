package com.lubbo.client.network;

import java.io.Closeable;

import com.lubbo.client.cluster.Provider;

/**
 * Created by benchu on 15/11/1.
 */
public interface Exchanger<I,O> extends Closeable {
    ResponseFuture<O> sendMessage(I message);


    boolean isAvailable();

    Provider getProvider();

}
