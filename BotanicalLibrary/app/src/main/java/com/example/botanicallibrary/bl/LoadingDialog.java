package com.example.botanicallibrary.bl;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.botanicallibrary.R;

public class LoadingDialog{
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity){
        this.activity=activity;
    }
    public void startDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.layout_loading,null));
        builder.setCancelable(false);

        alertDialog=builder.create();
        alertDialog.show();

    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
}