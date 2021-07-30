package com.example.cs4227_project.interceptor_pattern;

public class InterceptorFramework {
    private Dispatcher dispatcher;

    public InterceptorFramework(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void addInterceptor(Interceptor i) {
        dispatcher.attach(i);
    }

    public void removeInterceptor(Interceptor i) {
        dispatcher.detach(i);
    }

    public void executeRequest(InterceptorContext request) {
        dispatcher.dispatchInterceptors(request);
    }
}
