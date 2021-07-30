package com.example.cs4227_project.interceptor_pattern.dispatchers;

import com.example.cs4227_project.interceptor_pattern.Dispatcher;
import com.example.cs4227_project.interceptor_pattern.Interceptor;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.interceptor_pattern.Target;

import java.util.ArrayList;
import java.util.List;

public class PostMarshallDispatcher implements Dispatcher {
    private List<Interceptor> interceptors;
    private Target target;

    public PostMarshallDispatcher(Target target) {
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
        target.execute(request);
        for (Interceptor i : interceptors) {
            i.execute(request);
        }
    }
}
