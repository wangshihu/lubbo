package com.lubbo.core.protocol;

import java.util.Arrays;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.lubbo.core.message.LubboMessage;
import com.lubbo.core.message.MessageStatus;
import com.lubbo.core.message.MessageType;
import com.lubbo.core.message.SerializeType;

/**
 * Created by benchu on 15/10/25.
 */
public class LubboCodec {
    private SerializationFactory serializationFactory;
    public static final int TYPE_POS=4;
    public static final int ACTION_POS=5;
    public static final int STATUS_POS=6;
    public static final int SERIALIZE_POS=6;
    public static final int ID_POS=7;
    public static final int DATA_LENGTH_POS=16;

    /**
     * 讲lubboMessage解析成协议数组
     * @param message
     * @return
     */
    public  byte[] encode(LubboMessage message){
        byte[] data = serializationFactory.serialize(message.getSerializeType(), message.getValue());
        byte[] result = new byte[data.length+24];
        result[TYPE_POS]= (byte) message.getMessageType().getId();
        result[ACTION_POS] = message.getActionByte();
        result[SERIALIZE_POS] = (byte) message.getSerializeType().getId();
        result[STATUS_POS] = (byte) message.getStatus().getId();
        System.arraycopy(Longs.toByteArray(message.getId()),0,result,ID_POS,8);
        System.arraycopy(Ints.toByteArray(data.length),0,result,DATA_LENGTH_POS,4);
        return result;
    }

    /**
     * 讲协议数组解析成Lubbomessage
     * @return
     */
    public  LubboMessage decoder(byte[] bytes){
        LubboMessage result = new LubboMessage();
        //获得基本信息
        MessageType messageType = MessageType.get(bytes[TYPE_POS]);
        result.setMessageType(messageType);
        SerializeType serializeType = SerializeType.get(bytes[SERIALIZE_POS]);
        result.setSerializeType(serializeType);
        result.setStatus(MessageStatus.get(bytes[TYPE_POS]));
        result.setRequest((bytes[ACTION_POS] == 0));
        result.setId(Longs.fromByteArray(Arrays.copyOfRange(bytes, ID_POS, ID_POS + 8)));
        ///获得实际内容.
        int dataLength = Ints.fromByteArray(Arrays.copyOfRange(bytes,DATA_LENGTH_POS,DATA_LENGTH_POS+4));
        byte[] data = new byte[dataLength];
        System.arraycopy(bytes,24,data,0,dataLength);
        Object value = serializationFactory.unSerialize(serializeType, data, messageType.getClazz());
        result.setValue(value);
        return result;
    }

    public void setSerializationFactory(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }
}
