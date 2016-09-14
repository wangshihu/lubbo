package com.lubbo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.InvokerManager;
import com.lubbo.core.MsgHandler;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.network.MsgHandlerContext;

/**
 * 处理invocation 的请求
 *
 * @author benchu
 * @version on 15/11/1.
 */
public abstract class AbstractServerMessageHandler implements
    MsgHandler<LubboMessage<Invocation>, LubboMessage<Result>> {
    private static Logger logger = LoggerFactory.getLogger(AbstractServerMessageHandler.class);

    private InvokerManager<Invocation> invokerManager;

    protected void handlerMessage(LubboMessage<Invocation> request, MsgHandlerContext<LubboMessage<Result>> ctx) {
        Invocation invocation = request.getValue();
        Invoker invoker = invokerManager.getInvoker(invocation);
        Result result = invoker.invoke(invocation);
        //fixme 根据result的状态设置消息的状态.
        LubboMessage<Result> response = new LubboMessage<>(request, MessageStatus.NORMAL);
        response.setValue(result);
        ctx.respond(response);
    }

    public void setInvokerManager(InvokerManager<Invocation> invokerManager) {
        this.invokerManager = invokerManager;
    }
}
