package com.lubbo.client.network;

import java.util.concurrent.CompletableFuture;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public interface ResponseFutureFactory<I, O> {
    CompletableFuture<O> newResponseFuture(I msg);
}
