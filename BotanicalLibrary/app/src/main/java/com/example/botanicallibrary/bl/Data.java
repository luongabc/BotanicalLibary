package com.example.botanicallibrary.bl;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.en.response.demosetdata.ResponseSetData;
import com.example.botanicallibrary.en.response.demosetdata.Result;
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

public  class Data {
    public static void getSpecies(String key){
        RetrofitAPI getResponse =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.MY_API_PLANT)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitAPI.class);
        Call<ResponseSpecie> call=getResponse.getGbif(key);
        call.enqueue(new Callback<ResponseSpecie>() {
            @Override
            public void onResponse(Call<ResponseSpecie> call, Response<ResponseSpecie> response) {
                if(response.isSuccessful()){
                   Data.data(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseSpecie> call, Throwable t) {

            }
        });
    }
    public static void data(ResponseSpecie result){

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("default",      result.getCanonicalName());
        data.put("parentKey", result.getParentKey());
        data.put("rank","SPECIES");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("default", result.getGenus());
        data.put("parentKey", result.getFamilyKey());
        data.put("rank","GENUS");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getGenusKey()))
                .set(data, SetOptions.merge());
        data.clear();


        data.put("default", result.getFamily());
        data.put("parentKey", result.getOrderKey());
        data.put("rank","FAMILY");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getFamilyKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("default", result.getOrder());
        data.put("parentKey", result.getPhylumKey());
        data.put("rank","ORDER");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getOrderKey()))
                .set(data, SetOptions.merge());
        data.clear();


        data.put("default", result.getPhylum());
        data.put("parentKey", result.getClassKey());
        data.put("rank","PHYLUM");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getPhylumKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put("default", result.getClass_());
        data.put("parentKey", result.getKingdomKey());
        data.put("rank","CLASS");
        firebaseFirestore.collection("Botanicals")
                .document(String.valueOf(result.getClassKey()))
                .set(data, SetOptions.merge());
        data.clear();
    }
}
