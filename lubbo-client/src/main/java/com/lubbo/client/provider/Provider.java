package com.lubbo.client.provider;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public class Provider {
    private InetSocketAddress address;
    private int weight;

    public Provider(InetSocketAddress address) {
        this.address = address;
    }

    public Provider(InetSocketAddress address, int weight) {
        this.address = address;
        this.weight = weight;
    }


    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Provider provider = (Provider) o;
        return weight == provider.weight && Objects.equals(address, provider.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, weight);
    }

    @Override
    public String toString() {
        return "Provider{" +
                   "address=" + address +
                   ", weight=" + weight +
                   '}';
    }
}
