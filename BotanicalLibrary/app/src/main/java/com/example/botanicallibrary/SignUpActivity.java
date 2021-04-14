package com.example.botanicallibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String email,password;
    private boolean isSign=false;
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
            finish();
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
        dialog.startDialog(getString(R.string.loading));
        // ...
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toasty.success(getBaseContext(), getString(R.string.Success), Toast.LENGTH_SHORT, true).show();
                        SharedPreferences sp=getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.putString(Local.DeviceLocal.ID,user.getUid() );
                        Ed.putString(Local.DeviceLocal.EMAIL,email);
                        Ed.putString(Local.DeviceLocal.PASSWORD,password);
                        Ed.apply();
                        isSign=true;
                        finish();
                    } else {
                        Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                    }
                    dialog.dismissDialog();

                });
    }
    @Override
    public void finish() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean(Local.BundleLocal.ISSIGNIN,isSign);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}