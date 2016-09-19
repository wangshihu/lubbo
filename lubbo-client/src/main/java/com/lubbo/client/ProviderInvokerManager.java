package com.lubbo.client;

import com.lubbo.client.provider.Provider;

/**
 * @author benchu
 * @version 16/9/18.
 */
public interface ProviderInvokerManager {

    void addProvider(Provider provider);

    void removeProvider(Provider provider);

}
