package com.lubbo.client.network;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public interface ResponseSubscribe<K, E> {
    void subscribe(K key, ResponseListener<E> listener);
}
