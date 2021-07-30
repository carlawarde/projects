package com.example.cs4227_project.interceptor_pattern.dispatchers;

import com.example.cs4227_project.interceptor_pattern.Dispatcher;
import com.example.cs4227_project.interceptor_pattern.Interceptor;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.interceptor_pattern.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the attachment of interceptors prior to an event execution
 */
public class PreMarshallDispatcher implements Dispatcher {
    private List<Interceptor> interceptors;
    private Target target;

    public PreMarshallDispatcher(Target target) {
        interceptors = new ArrayList<>();
        this.target = target;
    }

    public void attach(Interceptor i) {
        interceptors.add(i);
    }

    public void detach(Interceptor i) {
        interceptors.remove(i);
    }

    public void dispatchInterceptors(InterceptorContext request) {
        for (Interceptor i : interceptors) {
            i.execute(request);
        }
        target.execute(request);
    }

}
