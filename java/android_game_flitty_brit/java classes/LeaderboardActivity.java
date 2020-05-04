package com.example.flittybrit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private String TAG = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ScoreItem> scoreArray = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ScoreItem score = new ScoreItem((String) data.get("displayName"), (Long) data.get("score"));
                                scoreArray.add(score);
                                populateListView(scoreArray);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d(TAG, "Retrieved data from firestore");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void populateListView(ArrayList<ScoreItem> scoreArray) {
        ScoreItemsAdapter adapter = new ScoreItemsAdapter(this, scoreArray);

        ListView listView = findViewById(R.id.leaderboardListView);
        listView.setAdapter(adapter);
    }
}
