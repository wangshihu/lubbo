package com.lubbo.server;

/**
 * Created by benchu on 15/11/1.
 */
public class ExposeConfig {
    private Class<?> serviceCLass;
    private Object targetBean;
    private String service;
    private int port;
    private String ip;

    public Class<?> getServiceCLass() {
        return serviceCLass;
    }

    public void setServiceCLass(Class<?> serviceCLass) {
        this.serviceCLass = serviceCLass;
        this.service = serviceCLass.getName();
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(Object targetBean) {
        this.targetBean = targetBean;
    }

    public String getService() {
        return service;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
