package com.example.botanicallibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.botanicallibrary.fragment.ListImageFragment;
import com.example.botanicallibrary.fragment.RealizePlantFragment;

import java.util.Objects;

public class BitanicalDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitanical_detail);
        Intent intent=getIntent();
        String key = (String) Objects.requireNonNull(intent.getExtras()).get("key");

        //key= "8848598";

        Bundle bundle=new Bundle();
        bundle.putString("key", key);
        ListImageFragment listImageFragment=ListImageFragment.newInstance(key);
        listImageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.f_listImage, listImageFragment,null)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}