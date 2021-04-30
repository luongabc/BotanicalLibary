package com.example.botanicallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.fragment.main.LibraryFragment;
import com.example.botanicallibrary.fragment.main.ListQuestionFragment;
import com.example.botanicallibrary.fragment.main.PrifileUserFragment;
import com.example.botanicallibrary.fragment.main.RealizePlantFragment;

public class ScreenMain extends AppCompatActivity {
    private String selected="";
    public ScreenMain(){
        super(R.layout.activity_screen_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageViewRealize=findViewById(R.id.realizePlant);
        ImageView library =findViewById(R.id.libraryPlant);
        ImageView userProfile=findViewById(R.id.profile);
        ImageView question =findViewById(R.id.iv_question);

        selected="LIBRARY";
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment, LibraryFragment.class,null)
                .commit();

        userProfile.setOnClickListener(v -> {
            if(selected.equals("PRIFILE")) return;
            selected="PRIFILE";
            PrifileUserFragment prifileUserFragment=new PrifileUserFragment();
            prifileUserFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment, prifileUserFragment, null)
                    .commit();
        });

        imageViewRealize.setOnClickListener(v -> {
            if(selected.equals("REALIZE")) return;
            selected="REALIZE";
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment, RealizePlantFragment.class,null)
                    .commit();
        });

        library.setOnClickListener(v -> {
            if(selected.equals("LIBRARY")) return;
            selected="LIBRARY";
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment, LibraryFragment.class,null)
                    .commit();
        });

        question.setOnClickListener(v-> {
            if(selected.equals("QUESTION")) return;
            selected="QUESTION";
            ListQuestionFragment listQuestionFragment=new ListQuestionFragment();
            listQuestionFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment, listQuestionFragment,null)
                    .commit();
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}