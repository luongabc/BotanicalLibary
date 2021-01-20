package com.example.botanicallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botanicallibrary.bl.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String email,password;
    private final String TAG="SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        EditText etEmail=findViewById(R.id.et_email);
        EditText etPassword=findViewById(R.id.et_password);
        EditText etRepassword=findViewById(R.id.et_repassword);
        Button btnSignUp =findViewById(R.id.btn_signUp);
        TextView tv_goToSignIn=findViewById(R.id.tv_signIn);

        //event click btn to go to activity sing in
        tv_goToSignIn.setOnClickListener(v->{
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            onDestroy();
        });

        //event click btn to sign up
        btnSignUp.setOnClickListener(v -> {
            if(!etPassword.getText().toString().equals(etRepassword.getText().toString())){
                Toast.makeText(this, etRepassword.getText()+" and "+ etPassword.getText(),Toast.LENGTH_LONG).show();
                return;
            }
            email=""+ etEmail.getText();
            password=""+etPassword.getText();
            createUserWithEmailAndPassword(email,password);
        });

    }
    private void createUserWithEmailAndPassword(String email,String password){
        LoadingDialog dialog=new LoadingDialog(this);
        dialog.startDialog("Sign up...");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        dialog.dismissDialog();

                    }
                    // ...
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    protected void signInFirebase(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    protected  void informationUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }
}