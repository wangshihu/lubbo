package com.lubbo.client.network;

/**
 * Created by benchu on 15/11/1.
 */
public interface ResponseListener<T> {
    void responseReceived(T message);
}
