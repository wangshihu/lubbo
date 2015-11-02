package com.lubbo.client.network;

/**
 * Created by benchu on 15/11/1.
 */
public interface ResponseFutureFactory<I,O> {
    ResponseFuture<O> newResponseFuture(I msg);
}
