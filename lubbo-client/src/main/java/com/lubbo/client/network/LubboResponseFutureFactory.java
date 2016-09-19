package com.lubbo.client.network;

import java.util.concurrent.CompletableFuture;

import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public class LubboResponseFutureFactory implements
    ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> {
    private ResponseSubscribe<Long, LubboMessage<Result>> subscribe;

    @Override
    public CompletableFuture<LubboMessage<Result>> newResponseFuture(LubboMessage<Invocation> msg) {
        FutureResult futureResult = new FutureResult();
        subscribe.subscribe(msg.getRequestId(), futureResult);
        return futureResult;
    }

    public void setSubscribe(ResponseSubscribe<Long, LubboMessage<Result>> subscribe) {
        this.subscribe = subscribe;
    }
}
