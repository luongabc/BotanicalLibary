package com.example.botanicallibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.botanicallibrary.fragment.BotanicalDescriptionFragment;
import com.example.botanicallibrary.fragment.LinkParentFragment;
import com.example.botanicallibrary.fragment.ListImageFragment;
import com.example.botanicallibrary.fragment.MapsWebViewFragment;
import com.example.botanicallibrary.fragment.RealizePlantFragment;

import java.util.Objects;

public class BitanicalDetailActivity extends AppCompatActivity {
    private String key;
    private static final String KEY="key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitanical_detail);
        Intent intent=getIntent();
        key = (String) Objects.requireNonNull(intent.getExtras()).get(KEY);

        Bundle bundle=new Bundle();
        bundle.putString(KEY, key);
        ListImageFragment listImageFragment=ListImageFragment.newInstance(key);
        listImageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.f_listImage, listImageFragment,null)
                .commit();

        ImageView listImg=findViewById(R.id.listImg);
        ImageView description=findViewById(R.id.description);
        ImageView linkParent=findViewById(R.id.linkParent);
        ImageView map=findViewById(R.id.map);

        map.setOnClickListener(v -> {
            MapsWebViewFragment mapsWebViewFragment=MapsWebViewFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage,mapsWebViewFragment,null)
                    .commit();
        });

        linkParent.setOnClickListener(v -> {
            LinkParentFragment linkParentFragment=LinkParentFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage,linkParentFragment,null)
                    .commit();
        });

        description.setOnClickListener(v -> {
            BotanicalDescriptionFragment botanicalDescriptionFragment=BotanicalDescriptionFragment.newInstance(key);
            botanicalDescriptionFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage, botanicalDescriptionFragment,null)
                    .commit();
        });
        listImg.setOnClickListener(v -> {
            ListImageFragment listImageFragment1 =ListImageFragment.newInstance(key);
            listImageFragment1.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.f_listImage, listImageFragment1,null)
                    .commit();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return  super.onCreateView(name, context, attrs);

    }
}