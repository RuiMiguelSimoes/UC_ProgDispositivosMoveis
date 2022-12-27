package com.example.happens;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class User{

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public String username;
    public String email;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User (String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void writeNewUser(String username,String userEmail, String password) {


        User newUser = new User(this.username, this.email,this.password);
        CollectionReference usersCollection = firestore.collection("Users");

        usersCollection.document(this.email).set(newUser);
    }

}
