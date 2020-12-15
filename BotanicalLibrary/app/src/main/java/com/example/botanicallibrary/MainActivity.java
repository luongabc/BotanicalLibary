package com.example.botanicallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.bl.Permission;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.en.response.demosetdata.ResponseSetData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.checkPermissionCamera(this);
        Permission.checkPermissionReadStorage(this);
        Permission.checkPermissionWriteStorage(this);
        ConstraintLayout constraintLayout=findViewById(R.id.lOBg);
        constraintLayout.setOnClickListener(v -> {
            Intent intent =new Intent(getBaseContext(), ScreenMain.class);//BitanicalDetailActivity
            startActivity(intent);
        });


    }
    protected void data(int n){
        DataListViewResponseRealize dataListViewResponseRealize;
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseSetData> call=getGbif.searchGbif("SPECIES",5,20*n);
        call.enqueue(new Callback<ResponseSetData>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSetData> call, @NotNull Response<ResponseSetData> response) {
                if (!response.isSuccessful() || response.body()==null ) return;
                assert response.body() != null;
                FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                for(int i=0;i<5;i++) {
                    if(response.body().getResults()==null) {
                        System.out.println(i+"nulllllllllllll");
                        return;
                    }
                    System.out.println(response.body().getResults().get(i).getKey());
                    Map<String, Object> data = new HashMap<>();
                    data.put("default",      response.body().getResults().get(i).getCanonicalName());
                    data.put("parentKey", response.body().getResults().get(i).getParentKey());
                    data.put("rank","SPECIES");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getKey()))
                            .set(data, SetOptions.merge());
                    data.clear();

                    data.put("default", response.body().getResults().get(i).getGenus());
                    data.put("parentKey", response.body().getResults().get(i).getFamilyKey());
                    data.put("rank","GENUS");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getGenusKey()))
                            .set(data, SetOptions.merge());
                    data.clear();


                    data.put("default", response.body().getResults().get(i).getFamily());
                    data.put("parentKey", response.body().getResults().get(i).getOrderKey());
                    data.put("rank","FAMILY");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getFamilyKey()))
                            .set(data, SetOptions.merge());
                    data.clear();

                    data.put("default", response.body().getResults().get(i).getOrder());
                    data.put("parentKey", response.body().getResults().get(i).getPhylumKey());
                    data.put("rank","ORDER");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getOrderKey()))
                            .set(data, SetOptions.merge());
                    data.clear();


                    data.put("default", response.body().getResults().get(i).getPhylum());
                    data.put("parentKey", response.body().getResults().get(i).getClassKey());
                    data.put("rank","PHYLUM");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getPhylumKey()))
                            .set(data, SetOptions.merge());
                    data.clear();

                    data.put("default", response.body().getResults().get(i).getClass_());
                    data.put("parentKey", response.body().getResults().get(i).getKingdomKey());
                    data.put("rank","CLASS");
                    firebaseFirestore.collection("Botanicals")
                            .document(String.valueOf(response.body().getResults().get(i).getClassKey()))
                            .set(data, SetOptions.merge());
                    data.clear();

                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseSetData> call, @NotNull Throwable t) {

            }
        });
    }
}