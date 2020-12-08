package com.example.botanicallibrary.bl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {

    public static void checkPermissionCamera(Context context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context,
                    Manifest.permission.CAMERA)) {
                showDialog(context,
                        Manifest.permission.CAMERA);

            } else {
                ActivityCompat.requestPermissions((Activity) context,new String[] { Manifest.permission.CAMERA },1);
            }
        }
    }
    public static void checkPermissionWriteStorage(Context context){

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showDialog(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions((Activity) context,new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
            }
        }
    }
    public static void checkPermissionReadStorage(Context context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showDialog(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions((Activity) context,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },1);
            }
        }
    }
    private static void showDialog(final Context context,
                                   final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("External storage" + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        new String[] { permission },
                        1));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
