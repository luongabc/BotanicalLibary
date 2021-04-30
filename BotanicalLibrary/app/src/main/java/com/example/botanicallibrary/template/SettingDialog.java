package com.example.botanicallibrary.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.botanicallibrary.LoginActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.SignUpActivity;
import com.example.botanicallibrary.en.Local;

public class SettingDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private boolean doneSignIn;
    public SettingDialog(Activity activity, boolean doneSignIn){
        if(activity==null) return;
        this.activity=activity;
        this.doneSignIn=doneSignIn;
    }
    @SuppressLint("SetTextI18n")
    public void startDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        ViewGroup viewGroup= (ViewGroup) layoutInflater.inflate(R.layout.layout_setting,null);
        TextView signIN=viewGroup.findViewById(R.id.tv_asignIn);
        TextView signUp=viewGroup.findViewById(R.id.tv_asignUp);
        ImageView btnClose=viewGroup.findViewById(R.id.im_aclose);

        btnClose.setOnClickListener(v->{
            dismissDialog();
        });
        if(doneSignIn) {
            signIN.setText("Đăng xuất");
            signUp.setHeight(0);
        }
        signIN.setOnClickListener(v->{
            Intent intent=new Intent(activity, LoginActivity.class);
            activity.startActivityForResult(intent, Local.REQUEST_CODE_GET_DATA);
            dismissDialog();
        });
        signUp.setOnClickListener(v->{
            Intent intent=new Intent(activity, SignUpActivity.class);
            activity.startActivity(intent);
            dismissDialog();
        });
        builder.setView(viewGroup);
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
