package com.example.botanicallibrary.Interface;


import com.example.botanicallibrary.en.Rss.RssFeed;
import com.example.botanicallibrary.en.response.ResponseDataPost;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.en.response.demosetdata.ResponseSetData;


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
    String MY_API_PLANT="https://my-api.plantnet.org/v2/identify/";
    String GBIF="https://api.gbif.org/";
    String RSSURL="http://ccttbvtvbinhduong.gov.vn/rssChanel/";


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
    Call<ResponseGbifMedia> getMedia(@Path("key") String key,@Query("limit") int limit,@Query("offset") int offset);

    //get
    @GET("v1/species/{key}")
    Call<ResponseSpecie> getGbif(@Path("key") String key);

    //test Data
    @GET("v1/species/search")
    Call<ResponseSetData> searchGbif(@Query("rank") String rank, @Query("limit") int limit, @Query("offset") int offset);

    //Rss
    @GET("tin-moi-nhat.rss")
    Call<RssFeed> getFeed();
}
