package com.lubbo.core.protocol;

import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.message.MessageType;
import com.lubbo.core.message.SerializeType;

import io.netty.buffer.ByteBuf;

/**
 * @author benchu
 * @version on 15/10/25.
 */
public class LubboCodec {
    private SerializationFactory serializationFactory;
    /**
     * 讲lubboMessage解析成协议数组
     */
    public void encode(LubboMessage message,ByteBuf out) {
        // 内容正文的contentLength不包含自身长度字段的长度，仅表示内容的长度，[0,INT_MAX)
        int dataLength = 0;
        // 表示数据包总长的值包含该字段的长度，表示整体的总长度，[4,INT_MAX)
        int totalLength = 0;

        // ---common header---
        int totalLengthIndex = out.writerIndex();
        out.writeInt(totalLength);// 此处只做占位 待数据写入完毕后修改此处的值

        out.writeByte((byte) message.getMessageType().getId());
        out.writeByte(message.getActionByte());
        out.writeByte((byte) message.getSerializeType().getId());
        out.writeByte((byte) message.getStatus().getId());
        out.writeLong(message.getRequestId());
        byte[] data = serializationFactory.serialize(message.getSerializeType(), message.getValue());
        out.writeInt(data.length);

        out.writeBytes(data);
        int endIndex = out.writerIndex();
        totalLength = endIndex - totalLengthIndex;
        out.setInt(totalLengthIndex, totalLength);// 修改totalLength的值
    }

    /**
     * 讲协议数组解析成LubboMessage
     */
    public LubboMessage decoder(ByteBuf in) {
        LubboMessage result = new LubboMessage();
        //获得基本信息
        int totalLength = in.readInt();
        MessageType messageType = MessageType.get(in.readByte());
        result.setRequest((in.readByte() == 0));
        result.setMessageType(messageType);
        SerializeType serializeType = SerializeType.get(in.readByte());
        result.setSerializeType(serializeType);
        result.setStatus(MessageStatus.get(in.readByte()));
        result.setRequestId(in.readLong());
        ///获得实际内容.
        int dataLength = in.readInt();

        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object value = serializationFactory.unSerialize(serializeType, data, messageType.getClazz());
        result.setValue(value);
        return result;
    }

    public void setSerializationFactory(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }
}
