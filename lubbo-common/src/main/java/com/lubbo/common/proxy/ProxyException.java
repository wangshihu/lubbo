package com.lubbo.common.proxy;

/**
 * 代理工厂类的异常封装
 * 
 * @author benchu
 * 
 */
public class ProxyException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ProxyException() {
        super();
    }

    public ProxyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(Throwable cause) {
        super(cause);
    }

}
