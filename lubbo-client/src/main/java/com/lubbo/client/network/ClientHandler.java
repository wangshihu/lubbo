package com.lubbo.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by benchu on 15-4-11.
 */
public class ClientHandler extends ChannelHandlerAdapter {
    private Logger logger  = LoggerFactory.getLogger(ClientHandler.class);



    /**
     * 当channel active的时间触发registry⌚️
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 读取逻辑,接收消息,然后分发到不同到handler中
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {

    }

    /**
     * 心跳的触发器.
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("channel has error",cause);
        ctx.close();
    }



}
