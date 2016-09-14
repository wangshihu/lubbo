package com.lubbo.client.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lubbo.core.MsgHandler;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.network.MsgHandlerContext;

/**
 * request时当消息注册器,id--listener,
 * response时,触发listener..
 * @author benchu
 * @version on 15/11/1.
 */
public class ResponseMessageHandler implements ResponseSubscribe<Long, LubboMessage<Result>>,
                                                   MsgHandler<LubboMessage<Result>, Void> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Long,ResponseListener<LubboMessage<Result>>> listeners = new ConcurrentHashMap<>();

    @Override
    public void subscribe(Long key, ResponseListener<LubboMessage<Result>> listener) {
        ResponseListener<LubboMessage<Result>> flag = this.listeners.put(key, listener);
        if (flag != null) {
            logger.error("override listener of msg,id is {}", key);
        }
    }

    @Override
    public void messageReceived(LubboMessage<Result> message, MsgHandlerContext<Void> ctx) {
        ResponseListener<LubboMessage<Result>> listener = this.listeners.remove(message.getRequestId());
        if (listener == null) {
            logger.error("can't find listener of {}", message);
        } else {
            listener.responseReceived(message);
        }
    }
}
