package com.lubbo.core.network.netty;

import java.io.IOException;

import com.lubbo.core.network.Channel;

class NettyChannel implements Channel {

    private final io.netty.channel.Channel channel;

    private final Integer id;

    public NettyChannel(io.netty.channel.Channel channel, Integer id) {
        this.channel = channel;
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void write(Object obj) {
        channel.write(obj);
    }

    @Override
    public void flush() {
        channel.flush();
    }

    @Override
    public void writeAndFlush(Object obj) {
        channel.writeAndFlush(obj);
    }

    @Override
    public void close() throws IOException {
        channel.close().syncUninterruptibly();
    }

    @Override
    public boolean isAvaiable() {
        return channel.isActive() && channel.isOpen();
    }

}
