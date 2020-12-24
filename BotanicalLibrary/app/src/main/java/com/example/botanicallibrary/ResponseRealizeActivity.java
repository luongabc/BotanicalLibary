package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.botanicallibrary.Interface.IResponseSpecie;
import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.fragment.ArrayAdapterResponseRealize;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResponseRealizeActivity extends AppCompatActivity {
    private ArrayAdapterResponseRealize arrayAdapterResponseRealize;

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
        arrayAdapterResponseRealize=new ArrayAdapterResponseRealize(getBaseContext(),R.layout.layout_card_response_realize, responseDataPost);
        lVResultRealize.setAdapter(arrayAdapterResponseRealize);
        ReceiverThread receiverThread=new ReceiverThread();
        receiverThread.run();
        for(int i = 0; i< responseDataPost.size(); i++){
            GetUrlImage( responseDataPost.get(i));
        }
        lVResultRealize.setOnItemClickListener((parent, view, position, id) -> {
            String key=((DataListViewResponseRealize)parent.getItemAtPosition(position)).getGbif();
            Intent intent1=new Intent(getBaseContext(),BitanicalDetailActivity.class);
            intent1.putExtra("key",key);
            startActivity(intent1);
        });


    }
    protected void GetUrlImage(DataListViewResponseRealize dataListViewResponseRealize){
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseGbifMedia> callMedia=getGbif.getMedia(dataListViewResponseRealize.getGbif(),1,0);
        callMedia.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(@NotNull Call<ResponseGbifMedia> call, @NotNull Response<ResponseGbifMedia> response) {
                if (!response.isSuccessful() || response.body().getResults().size()==0 ) return;
                assert response.body() != null;
                dataListViewResponseRealize.setUrl(response.body().getResults().get(0).getIdentifier());
                System.out.println(response.body().getResults().get(0).getTaxonKey());
            }
            @Override
            public void onFailure(@NotNull Call<ResponseGbifMedia> call, @NotNull Throwable t) {

            }
        });
        Call<ResponseSpecie> call=getGbif.getGbif(dataListViewResponseRealize.getGbif());
        call.enqueue(new Callback<ResponseSpecie>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSpecie> call, @NotNull Response<ResponseSpecie> response) {
                if (!response.isSuccessful() || response.body()==null ) return;
                assert response.body() != null;
                pust(response.body());

            }
            @Override
            public void onFailure(@NotNull Call<ResponseSpecie> call, @NotNull Throwable t) {

            }
        });
    }
    public void pust(ResponseSpecie responseSpecie){
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<>();

        data.put("parentKey",responseSpecie.getParentKey());
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(responseSpecie.getKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("defaul",responseSpecie.getGenus());
        data.put("parentKey",responseSpecie.getFamilyKey());
        firebaseFirestore.collection("Genus")
                .document(String.valueOf(responseSpecie.getGenusKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("parentKey",responseSpecie.getOrderKey());
        data.put("defaul",responseSpecie.getFamily());
        firebaseFirestore.collection("Familys")
                .document(String.valueOf(responseSpecie.getFamilyKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("defaul",responseSpecie.getOrder());
        data.put("parentKey",responseSpecie.getPhylumKey());
        firebaseFirestore.collection("Orders")
                .document(String.valueOf(responseSpecie.getOrderKey()))
                .set(data, SetOptions.merge());


        data.put("defaul",responseSpecie.getPhylum());
        data.put("parentKey",responseSpecie.getClassKey());
        firebaseFirestore.collection("Phylums")
                .document(String.valueOf(responseSpecie.getParentKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("defaul",responseSpecie.getClass_());
        firebaseFirestore.collection("Classes")
                .document(String.valueOf(responseSpecie.getClassKey()))
                .set(data, SetOptions.merge());
    }

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            ResponseRealizeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrayAdapterResponseRealize.notifyDataSetChanged();
                }
            });
        }
    }
}