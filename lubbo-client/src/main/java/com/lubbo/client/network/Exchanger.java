package com.lubbo.client.network;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public interface Exchanger<I, O> extends Closeable {
    CompletableFuture<O> send(I message);

    boolean isAvailable();

    Future<Void> asyncClose();
}
