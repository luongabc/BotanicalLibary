package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        EditText etEmail=findViewById(R.id.et_email);
        EditText etPassword=findViewById(R.id.et_password);
        EditText etPepassword=findViewById(R.id.et_repassword);
        Button btnSignUp =findViewById(R.id.btn_signUp);
        //TextView

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}