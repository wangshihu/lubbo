package com.lubbo.client.network;

/**
 * Created by benchu on 15/11/1.
 */
public interface ResponseSubscribe<K,E> {
    void subscribe(K key,ResponseListener<E>listener);
}
