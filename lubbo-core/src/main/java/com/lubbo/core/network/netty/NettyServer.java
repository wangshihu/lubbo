package com.lubbo.core.network.netty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lubbo.core.network.ConnectionConstans;
import com.lubbo.core.network.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public class NettyServer implements Server {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelInitializer channelInitializer;
    private AtomicInteger generator = new AtomicInteger(1);
    private Map<Integer,Channel> channelMap = new HashMap<>();
    /**
     * 连接超时时长
     */
    private int connectTimeoutMills = ConnectionConstans.DEFAULT_CONNECT_TIMEOUT_MILLS;

    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, ChannelInitializer channelInitializer) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.channelInitializer = channelInitializer;
    }
    @Override
    public void bind(int port) {
        ServerBootstrap bootstrap = prepareBootstrap();
        bootstrap.channel(NioServerSocketChannel.class).childHandler(this.channelInitializer);
        Channel channel = bootstrap.bind(port).syncUninterruptibly().channel();
        channelMap.put(generator.getAndIncrement(),channel);
        logger.debug("bind port:{} successfully", port);

    }

    public ServerBootstrap prepareBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
            .option(ChannelOption.SO_BACKLOG, 128).option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeoutMills);
        return bootstrap;
    }

    @Override
    public void close() throws IOException {
        for(Channel channel:channelMap.values()){
            channel.close().syncUninterruptibly();
        }
    }
}
