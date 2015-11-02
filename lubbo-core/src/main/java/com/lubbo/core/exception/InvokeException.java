package com.lubbo.core.exception;

/**
 * Created by benchu on 15/11/1.
 */
public class InvokeException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 2070202083659991410L;

    public InvokeException() {
    }

    public InvokeException(String message) {
        super(message);
    }

    public InvokeException(Throwable cause) {
        super(cause);
    }

    public InvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokeException(String message, Throwable cause,
                           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
