package com.lubbo.client;

/**
 * @author  benchu
 * @version on 15/10/24.
 */
public class ReferConfig<T> {
    private Class<T> targetClass;

    public ReferConfig(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }
}
