package com.lubbo.server.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.network.MsgHandlerContext;

/**
 * Created by benchu on 15/11/1.
 */
public class SynServerMessageHandler extends  AbstractServerMessageHandler {
    //fixme 处理任务的线程池
    private ExecutorService executorService= Executors.newCachedThreadPool();
    @Override
    public void execute(LubboMessage<Invocation> message, MsgHandlerContext<LubboMessage<Result>> ctx) {
        executorService.execute(() -> handlerMessage(message,ctx));
    }
}
