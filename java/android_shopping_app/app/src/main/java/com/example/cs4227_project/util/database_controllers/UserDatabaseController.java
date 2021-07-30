package com.example.cs4227_project.util.database_controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4227_project.database.Database;
import com.example.cs4227_project.database.UserReadListener;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDatabaseController {
    private final Database db = Database.getInstance();
    private final ArrayList<User> users = new ArrayList<>();
    private UserReadListener myEventL;
    private User user;

    public UserDatabaseController(){ }

    public UserDatabaseController(UserReadListener rl){
        this.myEventL = rl;
    }

    public void addUserToDB(User user, String id) {
        db.put("user", id, user);
    }

    public void getUserCollection() {
        users.clear();
        //get reference to collection from database
        CollectionReference colRef = db.get("user");
        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LogTags.DB_GET, document.getId() + " => " + document.getData());
                                //convert document to Product and add to List of data
                                readUserIntoList(document);
                            }
                            myEventL.userCallback("success");
                            Log.d(LogTags.DB_GET, "Number of products: " + users.size());
                        } else {
                            Log.d(LogTags.DB_GET, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getUserDoc(String id){
        DocumentReference docRef = db.get("user", id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = getUser(document.getData());
                        Log.d(LogTags.DB_GET, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(LogTags.DB_GET, "No such document");
                    }
                } else {
                    Log.d(LogTags.DB_GET, "get failed with ", task.getException());
                }
                myEventL.userCallback("success");
            }
        });
    }

    public User getUser(Map<String, Object> user){
        return new User((String)user.get("id"), (String)user.get("email"), (boolean)user.get("admin"));
    }

    public void readUserIntoList(QueryDocumentSnapshot document) {
        //Generate product from product factory
        User u = getUser(document.getData());
        users.add(u);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getSingleUser(){return user;}

}
