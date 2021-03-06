package com.example.botanicallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.bl.Data;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.fragment.responseRealize.ArrayAdapterResponseRealize;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResponseRealizeActivity extends AppCompatActivity {
    private ArrayAdapterResponseRealize arrayAdapterResponseRealize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_realize);
        Intent intent=getIntent();
        List<DataListViewResponseRealize> responseDataPost = (List<DataListViewResponseRealize>) (intent.getExtras()).getSerializable(Local.BundleLocal.RESPONSEREALIZE);
        ListView lVResultRealize=findViewById(R.id.lVResultRealize);

        assert responseDataPost != null;
        arrayAdapterResponseRealize=new ArrayAdapterResponseRealize(getBaseContext(),R.layout.layout_card_response_realize, responseDataPost);
        lVResultRealize.setAdapter(arrayAdapterResponseRealize);
        ReceiverThread receiverThread=new ReceiverThread();
        receiverThread.start();
        for(int i = 0; i< responseDataPost.size(); i++){
            getUrlImage( responseDataPost.get(i));
        }
        lVResultRealize.setOnItemClickListener((parent, view, position, id) -> {
            String key=((DataListViewResponseRealize)parent.getItemAtPosition(position)).getGbif();
            Intent intent1=new Intent(getBaseContext(),BitanicalDetailActivity.class);
            intent1.putExtra("key",key);
            startActivity(intent1);
        });
    }
    protected void getUrlImage(DataListViewResponseRealize dataListViewResponseRealize){
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);

        //get url image of botanical from gbif
        Call<ResponseGbifMedia> callMedia=getGbif.getMedia(dataListViewResponseRealize.getGbif(),1,0);
        callMedia.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(@NotNull Call<ResponseGbifMedia> call, @NotNull Response<ResponseGbifMedia> response) {
                if (!response.isSuccessful() || response.body().getResults().size()==0 ) return;
                assert response.body() != null;
                String url=response.body().getResults().get(0).getIdentifier();
                dataListViewResponseRealize.setUrl(url);
                Data.setUrlImageBg(dataListViewResponseRealize.getGbif(),url);
            }
            @Override
            public void onFailure(@NotNull Call<ResponseGbifMedia> call, @NotNull Throwable t) {

            }
        });

        //lấy thông tin họ hàng tu gbif thêm vào csdl
        Call<ResponseSpecie> call=getGbif.getGbif(dataListViewResponseRealize.getGbif());
        call.enqueue(new Callback<ResponseSpecie>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSpecie> call, @NotNull Response<ResponseSpecie> response) {
                if (!response.isSuccessful() || response.body()==null ) return;
                Data.data(response.body());
            }
            @Override
            public void onFailure(@NotNull Call<ResponseSpecie> call, @NotNull Throwable t) {

            }
        });
    }


    private class ReceiverThread extends Thread {
        @Override
        public void start() {
            ResponseRealizeActivity.this.runOnUiThread(() -> arrayAdapterResponseRealize.notifyDataSetChanged());
        }
    }

}