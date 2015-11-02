package com.lubbo.core.network.netty;

import java.io.Closeable;
import java.io.IOException;

import com.lubbo.common.NamedThreadFactory;
import com.lubbo.core.MsgHandler;
import com.lubbo.core.network.Client;
import com.lubbo.core.network.ConnectionConstans;
import com.lubbo.core.network.Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by benchu on 15/11/1.
 */
public class NettyNioTransport implements Closeable {
    private static final String BOSS_THREAD_NAME_PREFIX = "netty-boss";

    private static final String WOKER_THREAD_NAME_PREFIX = "netty-worker";

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private int workerThreads = ConnectionConstans.DEFAULT_WORKER_THREADS;

    private int bossThreads = ConnectionConstans.DEFAULT_BOSS_THREADS;
    private NettyCodecAdaptor adaptor;

    public Server getServer(MsgHandler serverMsgHandler) {
        ensureBossGroup();
        ensureWorkerGroup();
        ChannelProcessor processor = new ChannelProcessor(serverMsgHandler);
        ChannelInitializer channelInitializer = new DefaultChannelInitializer(processor,adaptor);
        return new NettyServer(bossGroup, workerGroup, channelInitializer);
    }

    public Client getClient(MsgHandler clientMsgHandler) {
        ensureWorkerGroup();
        ChannelProcessor<?, ?> processor = new ChannelProcessor<>(clientMsgHandler);
        ChannelInitializer channelInitializer = new DefaultChannelInitializer(processor,adaptor);
        return new NettyClient(channelInitializer, workerGroup);
    }

    private void ensureBossGroup() {
        if (this.bossGroup == null) {
            this.bossGroup = new NioEventLoopGroup(this.bossThreads, new NamedThreadFactory(BOSS_THREAD_NAME_PREFIX));
        }
    }

    private void ensureWorkerGroup() {
        if (this.workerGroup == null) {
            this.workerGroup =
                new NioEventLoopGroup(this.workerThreads, new NamedThreadFactory(WOKER_THREAD_NAME_PREFIX));
        }
    }

    // ------------ Setters ------------

    public void setWorkerThreads(int workerThreads) {
        if (workerThreads > 0) {
            this.workerThreads = workerThreads;
        }
    }

    public void setBossThreads(int bossThreads) {
        if (bossThreads > 0) {
            this.bossThreads = bossThreads;
        }
    }

    @Override
    public void close() throws IOException {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
    }

    public void setAdaptor(NettyCodecAdaptor adaptor) {
        this.adaptor = adaptor;
    }
}
