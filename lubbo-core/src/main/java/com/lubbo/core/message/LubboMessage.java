package com.lubbo.core.message;

/**
 * @author benchu
 * @version on 15/10/22.
 */
public class LubboMessage<E> {
    private MessageType messageType;
    private MessageStatus status;
    private SerializeType serializeType;
    private long requestId;
    private E value;

    public LubboMessage() {
    }

    public LubboMessage(MessageType messageType,  MessageStatus status, SerializeType serializeType) {
        this.messageType = messageType;
        this.status = status;
        this.serializeType = serializeType;
    }

    public LubboMessage(LubboMessage request, MessageStatus status) {
        this.requestId = request.requestId;
        this.messageType = request.messageType;
        this.serializeType = request.serializeType;
        this.status = status;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public SerializeType getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }


    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

}
