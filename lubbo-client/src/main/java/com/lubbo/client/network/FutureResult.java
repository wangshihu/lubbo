package com.lubbo.client.network;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;

/**
 * 参照futureTask实现的future监听器类.
 * 去掉run方法,修改set方法.
 * set方法类似触发器,解除阻塞
 * Created by benchu on 15/10/10.
 */
public class FutureResult extends ResponseFuture<LubboMessage<Result>> implements ResponseListener<LubboMessage<Result>> {
    public FutureResult() {
        super(() -> null);

    }

    @Override
    public void responseReceived(LubboMessage<Result> message) {
        super.set(message);
    }
}
