package com.lubbo.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benchu on 15/10/24.
 */
public class Result {
    //0.正常 1.error 2.超时
    private int status;

    private Object value;

    private String desc;

    private Throwable exception;

    private Map<String, Object> attachments;

    private transient Map<String, Object> resultContext;


    public Result() {
    }

    public Result(int status, Object value, String desc) {
        super();
        this.status = status;
        this.value = value;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getResultContext() {
        if (resultContext == null) {
            resultContext = new HashMap<>();
        }
        return resultContext;
    }

    public void setResultContext(Map<String, Object> resultContext) {
        this.resultContext = resultContext;
    }

    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        } else if (status != 0) {
            throw new Exception("remote execute error!  msg is :" + this.desc);
        }
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Result [status=").append(status).append(", value=").append(value).append(", desc=")
            .append(desc).append(", exception=").append(exception).append("]");
        return builder.toString();
    }
}
