package com.lubbo.client;

import java.util.List;

import com.lubbo.client.provider.Provider;

/**
 * @author benchu
 * @version 16/9/18.
 */
public interface Dictionary {

    List<Provider> getProviders(String service);

    void initService(String service);
}
