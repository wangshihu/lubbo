package com.lubbo.core.network.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.lubbo.core.network.Channel;
import com.lubbo.core.network.Client;
import com.lubbo.core.network.ConnectionConstans;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;

/**
 * Created by benchu on 15/11/1.
 */
public class NettyClient implements Client {
    private Bootstrap bootstrap;

    private EventLoopGroup workerGroup;

    private int connectTimeoutMills = ConnectionConstans.DEFAULT_CONNECT_TIMEOUT_MILLS;

    private ChannelInitializer<SocketChannel> channelInitializer;

    private final AtomicInteger channelSeq = new AtomicInteger();

    private final Map<Integer, Channel> connectedChannels = new ConcurrentHashMapV8<>();

    NettyClient(ChannelInitializer<SocketChannel> channelInitializer, EventLoopGroup workerGroup) {
        this.channelInitializer = channelInitializer;
        this.workerGroup = workerGroup;
        this.bootstrap = this.prepareBootstrap();
    }

    @Override
    public Channel connect(InetSocketAddress remoteAddress) {
        //复用bootstrap
        io.netty.channel.Channel channel = this.bootstrap.connect(remoteAddress).syncUninterruptibly().channel();
        NettyChannel nettyChannel = new NettyChannel(channel, this.channelSeq.incrementAndGet());
        this.connectedChannels.put(nettyChannel.getId(), nettyChannel);
        return nettyChannel;
    }

    private Bootstrap prepareBootstrap() {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeoutMills);
        b.channel(NioSocketChannel.class).handler(this.channelInitializer);
        return b;
    }

    @Override
    public void close() throws IOException {
        this.workerGroup.shutdownGracefully();
    }

    @Override
    public Channel getChannel(Integer channelId) {
        return this.connectedChannels.get(channelId);
    }
}
