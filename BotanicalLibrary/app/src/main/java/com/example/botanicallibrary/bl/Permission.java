package com.example.botanicallibrary.bl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    private static int REQUEST_CODE=100;
    public static void checkPermissionInternet(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.INTERNET},REQUEST_CODE);
        }
    }

    public static void checkPermissionCamera(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }
    }
    public static void checkPermissionWriteStorage(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }
    public static void checkPermissionReadStorage(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }
    public static void checkPermissionInteret(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{ Manifest.permission.ACCESS_NETWORK_STATE},REQUEST_CODE);
        }
    }
    public static boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    REQUEST_CODE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    private static void showDialog(final String msg, final Context context,
                                  final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                REQUEST_CODE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
