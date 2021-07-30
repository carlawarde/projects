package com.example.cs4227_project.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs4227_project.util.database_controllers.UserDatabaseController;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private UserDatabaseController userDb;
    private String bundleText;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userDb = new UserDatabaseController();

        if (getArguments().containsKey("toast")) {
            String toastText = getArguments().getString("toast");
            bundleText = toastText;
            Log.d(LogTags.LOG_IN, "Bundle Text: "+toastText);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);

        //Text fields
        mEmailField = rootView.findViewById(R.id.fieldEmail);
        mPasswordField = rootView.findViewById(R.id.fieldPassword);

        //Listeners
        rootView.findViewById(R.id.register).setOnClickListener(this);
        rootView.findViewById(R.id.signIn).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        if(bundleText != null) {
            Toast.makeText(getContext(), bundleText,
                    Toast.LENGTH_LONG).show();
        }

        super.onStart();
    }

    //Checks to make sure login credentials are valid
    private boolean validateForm(String email, String password){
        //Initially set to true and if form is invalid, boolean changes to false
        boolean valid = true;

        if(TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailField.setError("Invalid Format");
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else if(password.length()< 4){
            mPasswordField.setError("Must be greater than 4 characters");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return !valid;
    }

    //Method to register an account
    private void register(final String email, final String password) {
        //Checks credentials first
        if (validateForm(email, password)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(LogTags.REGISTER_ACCOUNT, "Sign-up successful");
                            Toast.makeText(getContext(), "Sign up successful.", Toast.LENGTH_LONG).show();
                            signIn(email, password);
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();
                            createUserProfile(userId, email);
                        } else {
                            Log.d(LogTags.REGISTER_ACCOUNT, "Sign-up failed");
                            Toast.makeText(getContext(), "Sign up failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Creates document for user in the database
    public void createUserProfile(String id, String email){
        User user = new User(id, email, false);
        userDb.addUserToDB(user, id);
    }

    //Firebase sign in method
    private void signIn(String email, String password) {
        //Checks credentials first
        if (validateForm(email, password)) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(LogTags.LOG_IN, "Logged in with "+user.getEmail());
                            //Close fragment when successfully logged in
                            getActivity().getSupportFragmentManager().popBackStackImmediate();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(LogTags.LOG_IN, "Failed to log in");
                            Toast.makeText(getContext(), "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signIn) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        if (i == R.id.register) {
            register(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}