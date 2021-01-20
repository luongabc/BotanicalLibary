package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.bl.Permission;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.en.response.demosetdata.ResponseSetData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout=findViewById(R.id.lOBg);
        constraintLayout.setOnClickListener(v -> {
            Intent intent =new Intent(getBaseContext(), ScreenMain.class);
            startActivity(intent);
        });
    }

}