package com.example.flittybrit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenu extends AppCompatActivity {
    String userEmail;
    FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    boolean loggedIn = false;
    private String TAG = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Instantiate Firebase and get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            //Get user information
            userEmail = user.getEmail();

            loggedIn = true;
        }
        //Updates menu options
        invalidateOptionsMenu();

        //Get score from intent if from GameView
        Bundle intentData = getIntent().getExtras();
        if(intentData != null) {
            if (intentData.getString("sender").equals("GameActivity")) {
                Log.d(TAG, "Received data from Game Acitvity");
                int score = intentData.getInt("score");
                Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //**************************************************\\
    //           BUTTON LISTENER METHODS                \\
    //**************************************************\\

    public void newGameButtonClick(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }

    public void leaderboardButtonClick(View view) {
        if(loggedIn && isOnline()) {
            Intent i = new Intent(this, LeaderboardActivity.class);
            startActivity(i);
        }
        else
            Toast.makeText(this,"You must be signed-in and have an internet connection to access the Leaderboards", Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //**************************************************\\
    // OPTION MENU METHODS AND SIGN-IN/SIGN-OUT METHODS \\
    //**************************************************\\

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch(item.getItemId()) {
            case R.id.overflow_sign_in:
                i = new Intent(this,LogInActivity.class);
                startActivity(i);
                return true;

            case R.id.overflow_sign_out:
                signOutUser();
                loggedIn = false;
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Enables and disables sign-in/sign-out buttons based on whether the user is currently signed-in or signed-out
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (loggedIn) {
            menu.findItem(R.id.overflow_sign_in).setEnabled(false);
            menu.findItem(R.id.overflow_sign_out).setEnabled(true);
        }
        else {
            menu.findItem(R.id.overflow_sign_in).setEnabled(true);
            menu.findItem(R.id.overflow_sign_out).setEnabled(false);
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public void signOutUser() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainMenu.this,"Signed out successfully from "+userEmail,Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                });
    }
}
