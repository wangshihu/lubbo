package com.lubbo.client.network;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by benchu on 15/11/1.
 */
public class ResponseFuture<V> extends FutureTask<V> {

    public ResponseFuture(Callable<V> callable) {
        super(callable);
    }
}
