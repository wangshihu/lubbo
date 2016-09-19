package com.lubbo.client.network;

import java.util.concurrent.CompletableFuture;

import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;

/**
 * 参照futureTask实现的future监听器类.
 * 去掉run方法,修改set方法.
 * set方法类似触发器,解除阻塞
 *
 * @author benchu
 * @version on 15/10/10.
 */
public class FutureResult extends CompletableFuture<LubboMessage<Result>> implements
    ResponseListener<LubboMessage<Result>> {

    @Override
    public void responseReceived(LubboMessage<Result> message) {
        super.complete(message);
    }
}
