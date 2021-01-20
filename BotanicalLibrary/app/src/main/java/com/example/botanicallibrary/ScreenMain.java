package com.example.botanicallibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.fragment.BotanicalDescriptionFragment;
import com.example.botanicallibrary.fragment.LibraryFragment;
import com.example.botanicallibrary.fragment.MapsWebViewFragment;
import com.example.botanicallibrary.fragment.NewsFragment;
import com.example.botanicallibrary.fragment.PrifileUserFragment;
import com.example.botanicallibrary.fragment.RealizePlantFragment;

public class ScreenMain extends AppCompatActivity {


    public ScreenMain(){
        super(R.layout.activity_screen_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageViewRealize=findViewById(R.id.realizePlant);
        ImageView library =findViewById(R.id.libraryPlant);
        ImageView news=findViewById(R.id.iv_news);
        ImageView userProfile=findViewById(R.id.profile);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment, NewsFragment.class,null)
                .commit();

        news.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment, NewsFragment.class,null)
                .commit());

        userProfile.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment, PrifileUserFragment.class,null)
                .commit());

        imageViewRealize.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment, RealizePlantFragment.class,null)
                .commit());
        library.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment, LibraryFragment.class,null)
                .commit());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}