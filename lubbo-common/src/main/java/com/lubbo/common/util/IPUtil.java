package com.lubbo.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author benchu
 * @version 16/9/14.
 */
public class IPUtil {
    /**
     * （1）先查找本机hostName；（2）根据hostName去dns查找对应的ip地址
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接根据第一个网卡地址作为其内网ipv4地址
     *
     * @return
     */
    public static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        return inet4Address.getHostAddress().toString();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress().toString();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static InetSocketAddress parseToSocketAddress(String hostPort) {
        String[] parts = hostPort.split(":", 2);
        return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));

    }

    public static int getAvailablePort(int begin) {
        boolean flag = isPortAvailable(begin);
        for (; !flag; begin++) {
            flag = isPortAvailable(begin);
        }
        return begin;
    }

    private static void bindPort(String host, int port) throws Exception {
        Socket s = new Socket();
        s.bind(new InetSocketAddress(host, port));
        s.close();
    }

    public static boolean isPortAvailable(int port) {
        try {
            bindPort("0.0.0.0", port);
            bindPort(getLocalIpByNetcard(), port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
