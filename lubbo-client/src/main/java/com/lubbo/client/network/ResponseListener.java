package com.lubbo.client.network;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public interface ResponseListener<T> {
    void responseReceived(T message);
}
