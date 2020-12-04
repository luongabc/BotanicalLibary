package com.example.botanicallibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.fragment.RealizePlantFragment;

public class ScreenMain extends AppCompatActivity {

    private ImageView imageViewRealize;

    public ScreenMain(){
        super(R.layout.activity_screen_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewRealize=findViewById(R.id.realizePlant);
        ImageView imgload=findViewById(R.id.imgload);
        imgload.setVisibility(View.GONE);
        imageViewRealize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment, RealizePlantFragment.class,null)
                        .commit();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}