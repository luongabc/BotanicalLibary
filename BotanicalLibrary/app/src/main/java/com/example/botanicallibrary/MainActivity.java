package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.botanicallibrary.bl.Permission;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.checkPermissionCamera(this);
        Permission.checkPermissionReadStorage(this);
        Permission.checkPermissionWriteStorage(this);
        ConstraintLayout constraintLayout=findViewById(R.id.lOBg);
        constraintLayout.setOnClickListener(v -> {
            Intent intent =new Intent(getBaseContext(), ScreenMain.class);//BitanicalDetailActivity
            startActivity(intent);
        });

    }
}