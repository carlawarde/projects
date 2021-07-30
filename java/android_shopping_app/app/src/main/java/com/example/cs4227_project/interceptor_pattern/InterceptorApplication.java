package com.example.cs4227_project.interceptor_pattern;

public class InterceptorApplication {
    private InterceptorFramework interceptorFramework;
    private static InterceptorApplication instance;

    public static InterceptorApplication getInstance() {
        if(instance == null) {
            instance = new InterceptorApplication();
        }
        return instance;
    }

    public void setInterceptorFramework(InterceptorFramework framework) {
        this.interceptorFramework = framework;
    }

    public void sendRequest(InterceptorContext request) {
        interceptorFramework.executeRequest(request);
    }
}
