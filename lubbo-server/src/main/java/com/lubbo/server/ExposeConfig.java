package com.lubbo.server;

/**
 * @author benchu
 * @version on 15/11/1.
 */
public class ExposeConfig {
    private Class<?> serviceClass;
    private Object targetBean;
    private String service;
    private int port;
    private String ip;

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        this.service = serviceClass.getName();
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
