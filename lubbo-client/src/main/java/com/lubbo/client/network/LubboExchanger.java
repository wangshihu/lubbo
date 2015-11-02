package com.lubbo.client.network;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.lubbo.client.cluster.Provider;
import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.message.MessageType;
import com.lubbo.core.message.SerializeType;
import com.lubbo.core.network.Channel;

/**
 * Created by benchu on 15/11/1.
 */
public class LubboExchanger implements Exchanger<Invocation, Result> {
    private Provider provider;
    private Channel channel;
    private static final AtomicInteger seqGenerator = new AtomicInteger();

    private ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> responseFutureFactory;

    public LubboExchanger(Provider provider, Channel channel,
                          ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> responseFutureFactory) {
        this.provider = provider;
        this.channel = channel;
        this.responseFutureFactory = responseFutureFactory;
    }

    @Override
    public ResponseFuture<Result> sendMessage(Invocation invocation) {
        if (!isAvailable()) {
            throw new IllegalStateException("LubboExchanger is not available");
        }
        LubboMessage<Invocation> request =
            new LubboMessage<>(MessageType.INVOCATION, true, MessageStatus.NORMAL, SerializeType.FAST_JSON);
        request.setId(seqGenerator.incrementAndGet());
        request.setValue(invocation);
        ResponseFuture<LubboMessage<Result>> future = responseFutureFactory.newResponseFuture(request);
        channel.writeAndFlush(request);
        return new ResponseFuture<Result>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public Result get() throws InterruptedException, ExecutionException {
                return future.get().getValue();
            }

            @Override
            public Result get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
                return future.get(timeout, unit).getValue();
            }
        };

    }

    @Override
    public boolean isAvailable() {
        if (channel == null) {
            return false;
        }
        return channel.isAvaiable();
    }

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

}
