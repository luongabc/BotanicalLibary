package com.example.botanicallibrary.bl;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.botanicallibrary.ArrayAdapterResponseRealize;
import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.ResponseRealizeActivity;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.PlantPost;
import com.example.botanicallibrary.en.response.ResponseDataPost;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.fragment.RealizePlantFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static  String MY_API_PLANT="https://my-api.plantnet.org/v2/identify/";
    private static  String GBIF="https://api.gbif.org/";


    public static void RealizePlant(Context context, ArrayList<PlantPost>  plantPosts){
        List<MultipartBody.Part> fileToUploads=new ArrayList<>();
        List<RequestBody> organs=new ArrayList<>();
        for(int i=0;i<plantPosts.size();i++){
            File file=new  File(Objects.requireNonNull(plantPosts.get(i).getFile().getPath()));
            RequestBody requestBody = RequestBody.create(file,MediaType.parse("multipart/form-data"));
            fileToUploads.add(MultipartBody.Part.createFormData("images", file.getName(), requestBody));

            organs.add(RequestBody.create(plantPosts.get(i).getOrgan(),MediaType.parse("multipart/form-data")));
        }
        RetrofitAPI getResponse =new Retrofit.Builder()
                .baseUrl(MY_API_PLANT)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitAPI.class);

        Call<ResponseDataPost> call;
        switch(plantPosts.size()){
            case 1 : call = getResponse.postImg(fileToUploads.get(0), organs.get(0));
                break;
            case 2: call = getResponse.postImgTwo(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1));
                break;
            case 3: call = getResponse.postImgThree(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2));
                break;
            case 4: call = getResponse.postImgFore(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2),fileToUploads.get(3), organs.get(3));
                break;
            case 5: call = getResponse.postImgFire(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2),fileToUploads.get(3), organs.get(3),fileToUploads.get(4), organs.get(4));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + plantPosts.size());
        }
        assert call != null;
        call.enqueue(new Callback<ResponseDataPost>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NotNull Call<ResponseDataPost> call, @NotNull Response<ResponseDataPost> response) {
                try{
                    //5
                    if(response.body()==null) {
                        Toast.makeText(context,"Fail",Toast.LENGTH_LONG);
                        return;
                    }
                    List<DataListViewResponseRealize> dataListViewResponseRealizes=new ArrayList<>();
                    for(int i=0;i<response.body().getResultRealizePlants().size();i++){
                        dataListViewResponseRealizes.add(new DataListViewResponseRealize(null
                                ,response.body().getResultRealizePlants().get(i).getSpecies().getScientificNameWithoutAuthor()
                                ,response.body().getResultRealizePlants().get(i).getScore().toString()
                                ,response.body().getResultRealizePlants().get(i).getGbif().getId()));
                    }
                    System.out.println(dataListViewResponseRealizes.size());
                    Intent intent=new Intent(context, ResponseRealizeActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("responseRealize", (Serializable) dataListViewResponseRealizes);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseDataPost> call, @NotNull Throwable t) {
                System.out.println("throww");
            }
        });
    }

    public static void GetUrlImage(ArrayAdapterResponseRealize arrayAdapterResponseRealize,DataListViewResponseRealize dataListViewResponseRealize){
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseGbifMedia> call=getGbif.getMedia(dataListViewResponseRealize.getGbif(),"1","0");
        call.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(Call<ResponseGbifMedia> call, Response<ResponseGbifMedia> response) {
                try{
                    if(response==null ||response.body().getResults().get(0).getIdentifier()==null)  return;
                    System.out.println(response.body().getResults().get(0).getIdentifier());
                    dataListViewResponseRealize.setUrl(response.body().getResults().get(0).getIdentifier());
                    arrayAdapterResponseRealize.notifyDataSetChanged();
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseGbifMedia> call, Throwable t) {

            }
        });
    }
    public static void GetGbifMedia(String key,String limit,String offset){
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseGbifMedia> call=getGbif.getMedia(key,limit,offset);
        call.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(Call<ResponseGbifMedia> call, Response<ResponseGbifMedia> response) {
                try{

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseGbifMedia> call, Throwable t) {

            }
        });

    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
