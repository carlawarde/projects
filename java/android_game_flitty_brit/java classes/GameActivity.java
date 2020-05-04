package com.example.flittybrit;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends Activity implements SurfaceHolder.Callback {

    private GameView gameView;
    private MainThread gameThread;
    private String TAG = "debug";

    private MediaPlayer music;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Object databaseScore = null;
    private CollectionReference scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up score database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        db.setFirestoreSettings(settings);

        scores = db.collection("scores");

        if(user != null) {
            checkUserHighscore();
        }

        //Set surfaceview, surface holder and thread
        gameView = new GameView(this);
        setContentView(gameView);
        SurfaceHolder gameHolder = gameView.getHolder();
        gameHolder.addCallback(this);
        gameThread = new MainThread(gameHolder, gameView);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Start game and thread
        gameView.makeLevel();
        music = MediaPlayer.create(this, R.raw.bensound_epic);
        music.start();
        gameThread.setRunning(true);
        gameThread.start();
        Log.d(TAG,"Thread Started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Backup to end thread if previous attempt failed
        if(gameThread.isAlive()) {
            endThread();
        }
        Log.d(TAG,"Thread Alive Status: "+gameThread.isAlive());
    }

    public void endThread() {
        gameThread.setRunning(false);
        stopDrawingThread();
    }

    public void stopDrawingThread() {
        gameThread.interrupt();
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    private void goBackToMainMenu(int score) {
        Intent i = new Intent(this, MainMenu.class);
        i.putExtra("score", score);
        i.putExtra("sender","GameActivity");
        startActivity(i);
        finish();
    }

    public void callGameActivity(int score) {
        updateScore(score);
        music.stop();
        music.release();
        endThread();
        goBackToMainMenu(score);
    }

    //To ensure music stops if user randomly backs out of app **CAUSES ERROR ATM**
    /*@Override
    protected void onPause() {
        super.onPause();
        music.stop();
        music.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        music.start();
    }*/

    //Write to database if user is logged-in
    public void updateScore(int score) {
        if( user != null) {
            long oldScore;

            if(databaseScore != null) {
                oldScore = (Long) databaseScore;
                Log.d(TAG, "Oldscore: " + oldScore);
                if (score > oldScore) {
                    // Create score entry with score and user email
                    logScore(score);
                }
            }
            else {
                logScore(score);
            }
        }
    }

    public void logScore(int score) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = df.format(new Date());
        Log.d(TAG, "Timestamp: "+timestamp);
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", timestamp);
        data.put("email", user.getEmail());
        data.put("displayName",shortenDisplayName(user.getDisplayName()));
        data.put("score", score);
        scores.document(user.getUid()).set(data);
        Log.d(TAG,java.text.DateFormat.getDateTimeInstance().format(new Date()));
    }

    public String shortenDisplayName(String username) {
        //Splits the users display name and returns the first word and first letter of the second word of the user's display name
        String[] array = username.split(" ");
        if(array.length  > 1)      return array[0] +" "+array[1].substring(0,1);
        else {                     return username;}
    }

    //Checks database for the user's previous score. If one exists it will assign it to the highscore Object
    public void checkUserHighscore() {
        DocumentReference docRef = db.collection("scores").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.i("dataReadSuccess", "DocumentSnapshot data: " + document.getData());

                        Map<String, Object> data = document.getData();
                        databaseScore = data.get("score");
                        Log.d(TAG,"Database Score: "+databaseScore);
                    }
                    else {
                        Log.i("noData", "No such document");
                    }
                }
                else {
                    Log.e("dataReadFail", "get failed with ", task.getException());
                }
            }
        });
    }
}
