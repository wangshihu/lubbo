package com.lubbo.server;

import java.util.Properties;

/**
 * Created by benchu on 15/10/24.
 */
public class ServerConfig {
    public String ip ;
    public int port ;
    public ServerConfig() {
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}