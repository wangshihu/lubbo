package com.lubbo.core.protocol;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lubbo.core.Invocation;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.message.MessageType;
import com.lubbo.core.message.SerializeType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author benchu
 * @version 16/9/14.
 */
public class LubboCodecTest {
    private LubboCodec lubboCodec = new LubboCodec();
    private long requestId = 123L;
    private String appName = "testApp";
    private String methodName = "testMethod";
    private String service = "com.test.service";
    private Object[] arguments = new Object[] {1, "22"};

    @Before
    public void before() {
        lubboCodec.setSerializationFactory(new SerializationFactory());
    }

    @Test
    public void decodeTest() {
        LubboMessage message = lubboCodec.decoder(encodeTest());
        assertEquals(message.getMessageType(), MessageType.INVOCATION);
        assertEquals(message.getStatus(), MessageStatus.NORMAL);
        assertEquals(message.getRequestId(), requestId);
        assertEquals(message.getSerializeType(), SerializeType.FAST_JSON);
        assertEquals(message.isRequest(), true);
        Invocation invocation = (Invocation) message.getValue();
        Assert.assertEquals(invocation.getAppName(), appName);
        Assert.assertEquals(invocation.getService(), service);
        Assert.assertEquals(invocation.getMethodName(), methodName);
        Object[] args = invocation.getArguments();
        Assert.assertEquals(args.length, 2);
        Assert.assertEquals(args[0], 1);
        Assert.assertEquals(args[1], "22");
    }

    public ByteBuf encodeTest() {
        ByteBuf out = UnpooledByteBufAllocator.DEFAULT.buffer();
        LubboMessage<Invocation> message = new LubboMessage<>();
        message.setRequestId(requestId);
        message.setRequest(true);
        message.setStatus(MessageStatus.NORMAL);
        message.setMessageType(MessageType.INVOCATION);
        message.setSerializeType(SerializeType.FAST_JSON);
        Invocation invocation = new Invocation();
        invocation.setAppName(appName);
        invocation.setMethodName(methodName);
        invocation.setService(service);
        invocation.setArguments(arguments);
        message.setValue(invocation);
        lubboCodec.encode(message, out);
        return out;
    }
}
