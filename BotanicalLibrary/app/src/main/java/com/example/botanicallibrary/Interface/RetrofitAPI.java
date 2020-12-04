package com.example.botanicallibrary.Interface;


import com.example.botanicallibrary.en.response.ResponseDataPost;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    //plantNet
    @Multipart
    @POST("all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp")
    Call<ResponseDataPost> postImg(@Part MultipartBody.Part file1, @Part("organs") RequestBody name1);

    @Multipart
    @POST("all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp")
    Call<ResponseDataPost> postImgTwo(@Part MultipartBody.Part file1, @Part("organs") RequestBody name1,@Part MultipartBody.Part file2, @Part("organs") RequestBody name2);

    @Multipart
    @POST("all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp")
    Call<ResponseDataPost> postImgThree(@Part MultipartBody.Part file1, @Part("organs") RequestBody name1,@Part MultipartBody.Part file2, @Part("organs") RequestBody name2,@Part MultipartBody.Part file3, @Part("organs") RequestBody name3);

    @Multipart
    @POST("all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp")
    Call<ResponseDataPost> postImgFore(@Part MultipartBody.Part file1, @Part("organs") RequestBody name1,@Part MultipartBody.Part file2, @Part("organs") RequestBody name2,@Part MultipartBody.Part file3, @Part("organs") RequestBody name3,@Part MultipartBody.Part file4, @Part("organs") RequestBody name4);

    @Multipart
    @POST("all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp")
    Call<ResponseDataPost> postImgFire(@Part MultipartBody.Part file1, @Part("organs") RequestBody name1,@Part MultipartBody.Part file2, @Part("organs") RequestBody name2,@Part MultipartBody.Part file3, @Part("organs") RequestBody name3,@Part MultipartBody.Part file4, @Part("organs") RequestBody name4,@Part MultipartBody.Part file5, @Part("organs") RequestBody name5);


    //Gbif
    //get media
    @GET("v1/species/{key}/media")
    Call<ResponseGbifMedia> getMedia(@Path("key") String key,@Query("limit") String limit,@Query("offset") String offset);


}
