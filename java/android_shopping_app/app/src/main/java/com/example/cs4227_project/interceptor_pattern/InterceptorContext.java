package com.example.cs4227_project.interceptor_pattern;

public class InterceptorContext {
    protected String message;
    protected Object data;

    public InterceptorContext(String message) {
        this.message = message;
    }

    public InterceptorContext(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
