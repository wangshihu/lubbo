package com.lubbo.core.network.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lubbo.core.network.ConnectionConstans;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChannelInitializer.class);
    private final ChannelHandler channelHandler;
    private int readIdleTimeout = ConnectionConstans.DEFAULT_READTIMEOUT;
    private int writeIdleTimeout = ConnectionConstans.DEFAULT_WRITETIMEOUT;
    private int idleTimeout = ConnectionConstans.DEFAULT_IDLETIMEOUT;

    private NettyCodecAdaptor adaptor;

    public DefaultChannelInitializer(ChannelHandler channelHandler, NettyCodecAdaptor adaptor, int readIdleTimeout,
                                     int writeIdleTimeout, int idleTimeout) {
        this.channelHandler = channelHandler;
        this.readIdleTimeout = readIdleTimeout;
        this.writeIdleTimeout = writeIdleTimeout;
        this.idleTimeout = idleTimeout;
        this.adaptor = adaptor;
    }

    public DefaultChannelInitializer(int writeIdleTimeout, NettyCodecAdaptor adaptor, ChannelHandler channelHandler) {
        this(channelHandler, adaptor, 0, writeIdleTimeout, 0);
    }

    public DefaultChannelInitializer(ChannelHandler channelHandler, NettyCodecAdaptor adaptor) {
        this(0, adaptor, channelHandler);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

//        // ----处理idle事件---
//        if (readIdleTimeout > 0 || writeIdleTimeout > 0 || idleTimeout > 0) {
//            pipeline.addLast("idleStateHandler", new IdleStateHandler(readIdleTimeout, writeIdleTimeout, idleTimeout));
//            pipeline.addLast("idleEventHandler", IdleEventHandler.INSTANCE);
//        }
        pipeline.addLast("decoder", adaptor.decoder());
        pipeline.addLast("encoder", adaptor.encoder());
        // --- 处理消息 ---
        pipeline.addLast("channlHandler", channelHandler);
    }

    /**
     * IdleEvent事件处理类，收到IdleStateEvent后则将ChannelHandlerContext关闭
     *
     * @author mozhu
     *
     */
    @Sharable
    private class IdleEventHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                switch (event.state()) {
                    default:
                        LOGGER.warn(event.state() + " ,channel closed.");
                        ctx.close();
                }
            }
        }

    }

    public void setAdaptor(NettyCodecAdaptor adaptor) {
        this.adaptor = adaptor;
    }
}
