package com.example.botanicallibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botanicallibrary.bl.Firebase;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth= FirebaseAuth.getInstance();
        Button btn_signIn=findViewById(R.id.btn_signIn);
        TextView tv_signUp=findViewById(R.id.tv_signUp);
        EditText et_email=findViewById(R.id.et_email);
        EditText et_pass=findViewById(R.id.et_pass);

        btn_signIn.setOnClickListener(v->{
            String email=""+et_email.getText().toString();
            String pass=""+et_pass.getText().toString();
            signIn(email,pass);
        });

        tv_signUp.setOnClickListener(v->{
            Intent intent=new Intent(getBaseContext(),SignUpActivity.class);
            startActivity(intent);
            onDestroy();
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=fAuth.getCurrentUser();
        //updateUI(firebaseUser);

    }
    void signIn(String email,String password){
        if(email==null||
                email==""||
                password==null||
                password=="") return;
        LoadingDialog dialog=new LoadingDialog(this);
        dialog.startDialog("Loading...");
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            //update


                            onDestroy();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        dialog.dismissDialog();
                        // ...
                    }
                });
    }

}