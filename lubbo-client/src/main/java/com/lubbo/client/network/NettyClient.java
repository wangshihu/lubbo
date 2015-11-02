package com.lubbo.client.network;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lubbo.common.Constants;
import com.lubbo.common.message.LubboMessage;
import com.lubbo.common.message.MessageStatus;
import com.lubbo.common.message.MessageType;
import com.lubbo.common.message.SerializeType;
import com.lubbo.common.protocol.LubboProtocol;
import com.lubbo.common.protocol.Serializations;
import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.network.AbstractNettyContainer;
import com.lubbo.core.network.FutureResult;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by benchu on 15/10/24.
 */
public class NettyClient extends AbstractNettyContainer {
    private static final int DEFAULT_HEARTBEAT_INTERVAL = 10;//
    private static final int DEFAULT_MESSAGE_WAIT = 2000;
    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private int port;
    private String host;
    private Channel channel;//该heartbeat拥有的channel
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private boolean started = false;

    public NettyClient(String providerHost) {
        String[] args = providerHost.split(":");
        this.port = Integer.valueOf(args[1]);
        this.host = args[0];

        this.started = true;
    }

    @Override
    public void run() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).group(eventLoopGroup)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(100, DEFAULT_HEARTBEAT_INTERVAL, 0));
                        pipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("encoder", new LengthFieldPrepender(4, false));
                        pipeline.addLast(new ClientHandler());
                    }
                });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            this.channel = future.channel();
            if (future.isSuccess()) {
                logger.info("heartbeat client start---host:" + host + "---port:" + port + "--------");
            }
            // 当代客户端链路关闭
            channel.closeFuture().sync();
        } catch (Exception e) {
            stop();
            logger.error("client channel has error ", e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    /**
     * 停止心跳,并且清理资源
     */
    @Override
    public void stop() {
        if (!started) {//防止container多次清理出异常
            return;
        }
        started = false;
        if (channel != null) {
            channel.close();
        }
        service.shutdownNow();
    }

    /**
     * 启动heartbeat
     */
    @Override
    public void start() {
        service.execute(this);
    }

    @Override
    public boolean isStart() {
        return started;
    }

    public Result invoke(Invocation invocation) {
        try {

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 客户端发送一些消息命令给服务器端.
     * 该方法是同步的
     *
     * @param message
     *
     * @return 结果信息
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */

    public LubboMessage sendMessage(LubboMessage message)
    //TODO await connect
        throws InterruptedException, ExecutionException, TimeoutException {
        if (logger.isDebugEnabled()) {
            logger.debug("client send message to server :" + message);
        }
        channel.writeAndFlush(Unpooled.copiedBuffer(LubboProtocol.encode(message)));
        FutureResult<LubboMessage> futureResult = new FutureResult<>();
        futureResultMap.put(message.getId(), futureResult);
        LubboMessage resultMessage = futureResult.get(DEFAULT_MESSAGE_WAIT, TimeUnit.MILLISECONDS);
        return resultMessage;
    }

    //    /**
    //     * 获得上次心跳的时间
    //     * @return
    //     */
    //    @Override
    //    public long getLastHeartBeatTimestamp() {
    //
    //        return globals.getLastHeartbeatTimestamp();
    //    }
    //
    //    /**
    //     * 停止心跳
    //     */
    //    @Override
    //    public void stopHeatBeat() {
    //        globals.setIsHeartbeat(false);
    //    }
    //
    //    /**
    //     * 验证schema和application是否匹配
    //     * @param application 应用名称
    //     * @param schema      库信息
    //     *
    //     * @return
    //     * @throws InterruptedException
    //     * @throws ExecutionException
    //     * @throws TimeoutException
    //     */
    //    @Override
    //    public boolean schemaAuth(String application, Schema schema)
    //        throws InterruptedException, ExecutionException, TimeoutException {
    //
    //        ResultMessage resultMessage = sendMessage(new SchemaAuthMessage(application,schema.getSchema()));
    //        return resultMessage.isSuccess();
    //    }
    //
    //    /**
    //     * 获得该实例的id
    //     * @return
    //     */
    //    @Override
    //    public long getRemoteId() {
    //        return globals.getId();
    //    }
    //

    //
    //    /**
    //     * 循环等待channel通道建立并且registry成功
    //     */
    //    private void awaitRegistry() {
    //        //如果没建立channel通道或者还没注册,那么循环等待.
    //        while (channel == null || !globals.isRegistry()) {
    //            try {
    //                TimeUnit.MILLISECONDS.sleep(500);
    //            } catch (InterruptedException e) {
    //            }
    //        }
    //    }
    //
    //    public ClientHandler getHandler(MessageType type) {
    //        return handlerRegistry.getHandler(type);
    //    }
    //
    //    public HeartbeatGlobals getGlobals() {
    //        return globals;
    //    }

}
