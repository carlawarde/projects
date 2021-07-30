package com.example.cs4227_project.interceptor_pattern.interceptors;

import android.util.Log;

import com.example.cs4227_project.interceptor_pattern.Interceptor;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.util.LogTags;

public class LoggingInterceptor implements Interceptor {
    @Override
    public void execute(InterceptorContext context) {
        String message = context.getMessage();
        Log.d(LogTags.INTERCEPTOR, message);
    }
}
