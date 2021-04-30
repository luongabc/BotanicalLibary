package com.example.botanicallibrary.bl;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

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
    public static void setUrlImageBg(String key,String url){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Map<String,Object> data=new HashMap<>();
        data.put(Local.IMAGEBG,url);
        System.out.println(key);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(key)
                .set(data, SetOptions.merge());
        data.clear();
    }

    public static void data(ResponseSpecie result){

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(Local.NAMEDEFAULT,      result.getCanonicalName());
        data.put(Local.PARENTKEY, result.getParentKey());
        data.put(Local.RANK,Local.SPECIES);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put(Local.NAMEDEFAULT, result.getGenus());
        data.put(Local.PARENTKEY, result.getFamilyKey());
        data.put(Local.RANK,Local.GENUS);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getGenusKey()))
                .set(data, SetOptions.merge());
        data.clear();


        data.put(Local.NAMEDEFAULT, result.getFamily());
        data.put(Local.PARENTKEY, result.getOrderKey());
        data.put(Local.RANK,Local.FAMILY);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getFamilyKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put(Local.NAMEDEFAULT, result.getOrder());
        data.put(Local.PARENTKEY, result.getPhylumKey());
        data.put(Local.RANK,Local.ORDER);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getOrderKey()))
                .set(data, SetOptions.merge());
        data.clear();


        data.put(Local.NAMEDEFAULT, result.getPhylum());
        data.put(Local.PARENTKEY, result.getClassKey());
        data.put(Local.RANK,Local.PHYLUM);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getPhylumKey()))
                .set(data, SetOptions.merge());
        data.clear();

        data.put(Local.NAMEDEFAULT, result.getClass_());
        data.put(Local.PARENTKEY, result.getKingdomKey());
        data.put(Local.RANK,Local.CLASS);
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(String.valueOf(result.getClassKey()))
                .set(data, SetOptions.merge());
        data.clear();
    }
}
