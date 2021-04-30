package com.example.botanicallibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.fragment.infoBotanical.BotanicalDescriptionFragment;
import com.example.botanicallibrary.fragment.infoBotanical.LinkParentFragment;
import com.example.botanicallibrary.fragment.infoBotanical.ListImageFragment;
import com.example.botanicallibrary.fragment.infoBotanical.MapsWebViewFragment;

import java.util.ArrayList;
import java.util.Objects;

public class BitanicalDetailActivity extends AppCompatActivity {
    private String key;
    private static final String KEY="key";
    private enum events{LISTIMAGE,LINKPARENT,MAP };
    ArrayList<ImageView> imageViews=new ArrayList<>();
    private events event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitanical_detail);
        Intent intent=getIntent();
        key = (String) Objects.requireNonNull(intent.getExtras()).get(KEY);

        Bundle bundle=new Bundle();
        bundle.putString(KEY, key);
        MapsWebViewFragment mapsWebViewFragment=MapsWebViewFragment.newInstance(bundle);

        LinkParentFragment linkParentFragment=LinkParentFragment.newInstance(bundle);

        BotanicalDescriptionFragment botanicalDescriptionFragment=BotanicalDescriptionFragment.newInstance(key);
        botanicalDescriptionFragment.setArguments(bundle);

        ListImageFragment listImageFragment1 =ListImageFragment.newInstance(key);
        listImageFragment1.setArguments(bundle);

        event=events.LISTIMAGE;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.f_listImage, listImageFragment1,null)
                .commit();
        imageViews.add(findViewById(R.id.listImg));
        imageViews.add(findViewById(R.id.linkParent));
        imageViews.add(findViewById(R.id.map));

        imageViews.get(0).setOnClickListener(v -> {
            if(event.equals(events.LISTIMAGE)) return;
            setEventBackground(event.ordinal(), events.LISTIMAGE.ordinal());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage, listImageFragment1,null)
                    .commit();
            event=events.LISTIMAGE;
        });
        imageViews.get(1).setOnClickListener(v -> {
            if(event.equals(events.LINKPARENT)) return;
            setEventBackground(event.ordinal(), events.LINKPARENT.ordinal());
            getSupportFragmentManager().beginTransaction()
                    .show(linkParentFragment);getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage, linkParentFragment,null)
                    .commit();
            event=events.LINKPARENT;
        });
        imageViews.get(2).setOnClickListener(v -> {
            if(event.equals(events.MAP)) return;
            setEventBackground(event.ordinal(), events.MAP.ordinal());
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.f_listImage, mapsWebViewFragment,null)
                    .commit();
            event=events.MAP;
        });
    }
    private void setEventBackground(int oldEvent,int newEvent){
        imageViews.get(newEvent).setBackgroundResource(R.color.white);
        imageViews.get(oldEvent).setBackgroundResource(0);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return  super.onCreateView(name, context, attrs);
    }
}