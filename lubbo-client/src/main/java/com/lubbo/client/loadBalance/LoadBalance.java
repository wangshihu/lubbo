package com.lubbo.client.loadBalance;

import java.util.List;

import com.lubbo.client.provider.Provider;
import com.lubbo.core.Invocation;

/**
 * @author benchu
 * @version 16/9/18.
 */
public interface LoadBalance {
    Provider select(List<? extends Provider> providers, Invocation invocation);
}
