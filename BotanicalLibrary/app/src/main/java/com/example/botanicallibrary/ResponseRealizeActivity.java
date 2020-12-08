package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResponseRealizeActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_realize);
        Intent intent=getIntent();
        List<DataListViewResponseRealize> responseDataPost = (List<DataListViewResponseRealize>) Objects.requireNonNull(intent.getExtras()).getSerializable("responseRealize");
        ListView lVResultRealize=findViewById(R.id.lVResultRealize);

        assert responseDataPost != null;
        ArrayAdapterResponseRealize arrayAdapterResponseRealize=new ArrayAdapterResponseRealize(getBaseContext(),R.layout.activity_card_response_realize, responseDataPost);
        lVResultRealize.setAdapter(arrayAdapterResponseRealize);
        for(int i = 0; i< responseDataPost.size(); i++){
            GetUrlImage(arrayAdapterResponseRealize, responseDataPost.get(i));
        }
        lVResultRealize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key=((DataListViewResponseRealize)parent.getItemAtPosition(position)).getGbif();
                Intent intent1=new Intent(getBaseContext(),BitanicalDetailActivity.class);
                intent1.putExtra("key",key);
                startActivity(intent1);
            }
        });


    }
    protected void GetUrlImage(ArrayAdapterResponseRealize arrayAdapterResponseRealize,DataListViewResponseRealize dataListViewResponseRealize){
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseGbifMedia> call=getGbif.getMedia(dataListViewResponseRealize.getGbif(),1,0);
        call.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(@NotNull Call<ResponseGbifMedia> call, @NotNull Response<ResponseGbifMedia> response) {
                if (!response.isSuccessful()) return;
                assert response.body() != null;
                dataListViewResponseRealize.setUrl(response.body().getResults().get(0).getIdentifier());
                arrayAdapterResponseRealize.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseGbifMedia> call, @NotNull Throwable t) {

            }
        });
    }
}