package com.lubbo.client.network;

import java.util.List;

import com.lubbo.client.cluster.NettyCluster;
import com.lubbo.core.Invocation;
import com.lubbo.core.Invoker;
import com.lubbo.core.Result;

/**
 * Created by benchu on 15/10/24.
 */
public class ClientInvoker implements Invoker {

    private NettyCluster nettyCluster ;

    public ClientInvoker(List<String> providers) {
        nettyCluster = new NettyCluster(providers);

    }

    @Override
    public Result invoke(Invocation invocation) {
        NettyClient client = nettyCluster.getNettyClient();
        return client.invoke(invocation);
    }

    public NettyCluster getCluster() {
        return nettyCluster;
    }
}
