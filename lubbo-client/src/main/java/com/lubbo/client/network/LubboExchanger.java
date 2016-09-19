package com.lubbo.client.network;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.lubbo.core.Invocation;
import com.lubbo.core.Result;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.message.MessageType;
import com.lubbo.core.message.SerializeType;
import com.lubbo.core.network.Channel;

/**
 * @author  benchu
 * @version on 15/11/1.
 */
public class LubboExchanger implements Exchanger<Invocation, Result> {
    private Channel channel;
    private static final AtomicInteger seqGenerator = new AtomicInteger();

    private ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>> responseFutureFactory;

    public LubboExchanger() {
    }

    @Override
    public CompletableFuture<Result> send(Invocation invocation) {
        if (!isAvailable()) {
            throw new IllegalStateException("LubboExchanger is not available");
        }
        LubboMessage<Invocation> request =
            new LubboMessage<>(MessageType.INVOCATION, MessageStatus.NORMAL, SerializeType.FAST_JSON);
        request.setRequestId(seqGenerator.incrementAndGet());
        request.setValue(invocation);
        CompletableFuture<LubboMessage<Result>> rawFuture = responseFutureFactory.newResponseFuture(request);
        channel.writeAndFlush(request);
        return new CompletableFuture<Result>(){
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return rawFuture.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return rawFuture.isCancelled();
            }

            @Override
            public Result get() throws InterruptedException, ExecutionException {
                return rawFuture.get().getValue();
            }

            @Override
            public Result get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
                return rawFuture.get(timeout, unit).getValue();
            }

            @Override
            public boolean isDone() {
                return rawFuture.isDone();
            }
        };

    }

    @Override
    public boolean isAvailable() {
        return channel != null && channel.isAvailable();
    }

    @Override
    public Future<Void> asyncClose() {
        return null;
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }


    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setResponseFutureFactory(ResponseFutureFactory<LubboMessage<Invocation>, LubboMessage<Result>>
                                             responseFutureFactory) {
        this.responseFutureFactory = responseFutureFactory;
    }
}
