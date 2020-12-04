package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.botanicallibrary.bl.RetrofitService;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseDataPost;
import com.example.botanicallibrary.en.response.ResultRealizePlant;

import java.util.ArrayList;
import java.util.List;

public class ResponseRealizeActivity extends AppCompatActivity {
    private List<DataListViewResponseRealize> responseDataPost;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ResponseRealizeActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_realize);
        Intent intent=getIntent();
        responseDataPost= (List<DataListViewResponseRealize>) intent.getExtras().getSerializable("responseRealize");
        ListView lVResultRealize=findViewById(R.id.lVResultRealize);

        ArrayAdapterResponseRealize arrayAdapterResponseRealize=new ArrayAdapterResponseRealize(getBaseContext(),R.layout.activity_card_response_realize,responseDataPost);
        lVResultRealize.setAdapter(arrayAdapterResponseRealize);
        for(int i=0;i<responseDataPost.size();i++){
            System.out.println(i);
            RetrofitService.GetUrlImage(arrayAdapterResponseRealize,responseDataPost.get(i));
        }

    }
}