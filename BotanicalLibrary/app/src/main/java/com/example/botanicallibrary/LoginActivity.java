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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private boolean islogin=false;
    private String email;
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
            email=""+et_email.getText().toString();
            String pass=""+et_pass.getText().toString();
            signIn(email,pass);
        });

        tv_signUp.setOnClickListener(v->{
            Intent intent=new Intent(getBaseContext(),SignUpActivity.class);
            startActivity(intent);
            finish();

        });

        SharedPreferences sp=getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.remove(Local.DeviceLocal.EMAIL);
        Ed.remove(Local.DeviceLocal.PASSWORD);
        Ed.remove(Local.DeviceLocal.ID);
        Ed.apply();
    }
    @Override
    public void onStart() {
        super.onStart();
        //updateUI(firebaseUser);

    }
    void signIn(String email,String password){
        if(email==null||
                email.equals("") ||
                password==null||
                password.equals("")) return;

        LoadingDialog dialog=new LoadingDialog(this);
        dialog.startDialog(getString(R.string.loading));
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //save email and password in device
                        Toasty.success(getBaseContext(),getString( R.string.Success), Toast.LENGTH_SHORT, true).show();
                        SharedPreferences sp=getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
                        SharedPreferences.Editor Ed=sp.edit();
                        FirebaseUser user = fAuth.getCurrentUser();
                        Ed.putString(Local.DeviceLocal.ID,user.getUid() );
                        Ed.putString(Local.DeviceLocal.EMAIL,email );
                        Ed.putString(Local.DeviceLocal.PASSWORD,password);
                        Ed.apply();
                        islogin=true;
                        finish();
                    } else {
                        islogin=false;
                        // If sign in fails, display a message to the user.
                        Toasty.error(getBaseContext(),getString( R.string.Error), Toast.LENGTH_SHORT, true).show();
                        //updateUI(null);
                    }
                    dialog.dismissDialog();
                    // ...
                });
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean(Local.BundleLocal.ISSIGNIN,islogin);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}