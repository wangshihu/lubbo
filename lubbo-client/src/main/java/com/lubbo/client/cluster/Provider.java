package com.lubbo.client.cluster;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by benchu on 15/11/1.
 */
public class Provider {
    private InetSocketAddress address;

    public Provider(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}
