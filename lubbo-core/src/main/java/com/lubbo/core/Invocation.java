package com.lubbo.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  benchu
 * @version on 15/10/24.
 */
public class Invocation {
    private String appName;
    private String methodName;
    private String service;
    /**
     * 调用参数
     */
    private Object[] arguments;
    /**
     * 用于传输附加参数
     */

    private Map<String, Object> attachments = new HashMap<>();
//    /**
//     * construct的时候不会初始化
//     */
//    private transient Class<?>[] argTypes;

    public Invocation() {
    }

    public Invocation(String methodName, String service, Object[] arguments) {
        this.methodName = methodName;
        this.service = service;
        this.arguments = arguments;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Class[] argTypes(){
        Class[] argTypes=new Class[arguments.length];
        for(int i=0;i<argTypes.length;i++){
            argTypes[i]=arguments.getClass();
        }
        return argTypes;
    }
}
