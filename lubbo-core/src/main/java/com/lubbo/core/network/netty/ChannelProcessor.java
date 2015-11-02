/*
 * Copyright 2011-2014 Mogujie Co.Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lubbo.core.network.netty;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.lubbo.core.MsgHandler;
import com.lubbo.core.network.MsgHandlerContext;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Channel消息处理类
 *
 * @author benchu
 *
 * @param <I>
 *            输入消息类型
 * @param <O>
 *            输出消息类型
 */
@Sharable
class ChannelProcessor<I, O> extends SimpleChannelInboundHandler<I> implements Closeable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<Channel, Integer> channels = new ConcurrentHashMap<>();

    private final AtomicInteger seq = new AtomicInteger();

    //private TypeParameterMatcher matcher;

    private MsgHandler<I, O> msgHandler;


    @SuppressWarnings("unchecked")
    public ChannelProcessor(MsgHandler<I, O> msgHandler) {
        this.msgHandler = msgHandler;
        //this.matcher = TypeParameterMatcher.get(msgHandler.getSupportedMsgType());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       messageReceived(ctx, (I) msg);
    }
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, I msg) throws Exception {
        msgHandler.execute( msg, new MsgHandlerContext<O>() {
            @Override
            public void respond(O resp) {
                ctx.writeAndFlush(resp);
            }

            @Override
            public InetSocketAddress remoteAddress() {
                return (InetSocketAddress) ctx.channel().remoteAddress();
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("channel active. channel：{}", channel);
        this.channels.put(channel, this.seq.getAndIncrement());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} close", ctx.channel());
        this.seq.decrementAndGet();
        this.channels.remove(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("netty exception occur!", cause);
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void close() {
        for (Channel channel : this.channels.keySet()) {
            channel.close().syncUninterruptibly();
        }
    }

    public void setMsgHandler(MsgHandler<I, O> msgHandler) {
        this.msgHandler = msgHandler;
    }

}
