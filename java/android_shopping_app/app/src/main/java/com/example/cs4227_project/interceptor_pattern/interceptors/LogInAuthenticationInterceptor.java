package com.example.cs4227_project.interceptor_pattern.interceptors;

import android.os.Bundle;
import android.util.Log;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptor_pattern.InterceptorContext;
import com.example.cs4227_project.interceptor_pattern.Interceptor;
import com.example.cs4227_project.util.ui_controllers.FragmentController;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.user.LogInFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogInAuthenticationInterceptor implements Interceptor {

    @Override
    public void execute(InterceptorContext context) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(LogTags.INTERCEPTOR, "User is not logged-in. Cannot start checkout process");
            FragmentController fragmentController = FragmentController.getInstance();
            Bundle bundle = new Bundle();
            bundle.putString("toast", context.getMessage());
            context.setMessage("Launching log-in screen");
            fragmentController.startFragment(new LogInFragment(), R.id.content, "log_in", bundle);
        }
        else {
            context.setMessage("User already logged-in");
        }
    }
}
