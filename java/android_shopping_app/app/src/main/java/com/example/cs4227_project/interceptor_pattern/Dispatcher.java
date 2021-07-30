package com.example.cs4227_project.interceptor_pattern;

public interface Dispatcher {
    public void attach(Interceptor i);

    public void detach(Interceptor i);

    public void dispatchInterceptors(InterceptorContext request);
}
