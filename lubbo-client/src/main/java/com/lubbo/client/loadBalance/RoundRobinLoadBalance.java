package com.lubbo.client.loadBalance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.benchu.lu.utils.MapUtils;
import com.lubbo.client.provider.Provider;
import com.lubbo.core.Invocation;

/**
 * 简单的轮询
 * @author benchu
 * @version 16/9/30.
 */
public class RoundRobinLoadBalance implements LoadBalance {
    private Map<String, AtomicLong> indexMap = new ConcurrentHashMap<>();

    @Override
    public Provider select(List<? extends Provider> providers, Invocation invocation) {
        String service = invocation.getService();
        AtomicLong al = MapUtils.putIfNull(indexMap, service, AtomicLong::new);
        long current = al.getAndIncrement();
        long index = current % providers.size();
        return providers.get((int) index);
    }

}
