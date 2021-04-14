package com.example.botanicallibrary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private boolean isSignIn=false;
    private SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth=FirebaseAuth.getInstance();
        ConstraintLayout constraintLayout = findViewById(R.id.lOBg);
        sp1=this.getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
        signIn(sp1.getString(Local.DeviceLocal.EMAIL, null),
                sp1.getString(Local.DeviceLocal.PASSWORD, null));
        constraintLayout.setOnClickListener(v -> {
            Intent intent =new Intent(getBaseContext(), ScreenMain.class);
            Bundle bundle =new Bundle();
            bundle.putBoolean(Local.BundleLocal.ISSIGNIN,isSignIn);
            intent.putExtras(bundle);
            startActivity(intent);
        });
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
                    SharedPreferences.Editor Ed=sp1.edit();
                    if (task.isSuccessful()) {
                        isSignIn=true;
                        FirebaseUser user = fAuth.getCurrentUser();
                        Ed.putString(Local.DeviceLocal.ID,user.getUid() );
                        Ed.apply();
                        Toasty.success(getBaseContext(),getString(R.string.Success), Toast.LENGTH_SHORT, true).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Ed.remove(Local.DeviceLocal.ID);
                        Toasty.error(getBaseContext(),getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                    }
                    dialog.dismissDialog();
                });
    }

}