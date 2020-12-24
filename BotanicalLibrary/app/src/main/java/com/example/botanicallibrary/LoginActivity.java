package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.botanicallibrary.bl.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth= FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=fAuth.getCurrentUser();
        //updateUI(firebaseUser);

    }

}