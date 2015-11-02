package com.lubbo.client.network;

import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;

/**
 * Created by benchu on 15/11/1.
 */
public class LubboResponseFutureFactory implements ResponseFutureFactory<LubboMessage<Invocation>,
                                                                            LubboMessage<Result>> {
    private ResponseSubscribe subscribe;
    @Override
    public ResponseFuture<LubboMessage<Result>> newResponseFuture(LubboMessage<Invocation> msg) {
        FutureResult futureResult = new FutureResult();
        subscribe.subscribe(msg.getId(),futureResult);
        return futureResult;
    }

    public void setSubscribe(ResponseSubscribe subscribe) {
        this.subscribe = subscribe;
    }
}