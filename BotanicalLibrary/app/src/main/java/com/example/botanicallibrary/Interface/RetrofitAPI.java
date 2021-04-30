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
    String MY_API_PLANT="https://my-api.plantnet.org/v2/identify/",
         GBIF="https://api.gbif.org/",
         RSSURL="http://ccttbvtvbinhduong.gov.vn/rssChanel/",
         PLANTNETKEY="all?api-key=2a10uHE2Tk4h0wuQSvnDAdPp",
         ORGANS="organs",
         GBGETMEDIA="v1/species/{key}/media",
         GBGET="v1/species/{key}",
         KEY="key",RANK="rank",LIMIT="limit",OFFSET="offset";
    //plantNet
    @Multipart
    @POST(PLANTNETKEY)
    Call<ResponseDataPost> postImg(@Part MultipartBody.Part file1, @Part(ORGANS) RequestBody name1);

    @Multipart
    @POST(PLANTNETKEY)
    Call<ResponseDataPost> postImgTwo(@Part MultipartBody.Part file1, @Part(ORGANS) RequestBody name1,@Part MultipartBody.Part file2, @Part(ORGANS) RequestBody name2);

    @Multipart
    @POST(PLANTNETKEY)
    Call<ResponseDataPost> postImgThree(@Part MultipartBody.Part file1, @Part(ORGANS) RequestBody name1,@Part MultipartBody.Part file2, @Part(ORGANS) RequestBody name2,@Part MultipartBody.Part file3, @Part(ORGANS) RequestBody name3);

    @Multipart
    @POST(PLANTNETKEY)
    Call<ResponseDataPost> postImgFore(@Part MultipartBody.Part file1, @Part(ORGANS) RequestBody name1,@Part MultipartBody.Part file2, @Part(ORGANS) RequestBody name2,@Part MultipartBody.Part file3, @Part(ORGANS) RequestBody name3,@Part MultipartBody.Part file4, @Part(ORGANS) RequestBody name4);

    @Multipart
    @POST(PLANTNETKEY)
    Call<ResponseDataPost> postImgFire(@Part MultipartBody.Part file1, @Part(ORGANS) RequestBody name1,@Part MultipartBody.Part file2, @Part(ORGANS) RequestBody name2,@Part MultipartBody.Part file3, @Part(ORGANS) RequestBody name3,@Part MultipartBody.Part file4, @Part(ORGANS) RequestBody name4,@Part MultipartBody.Part file5, @Part(ORGANS) RequestBody name5);


    //Gbif
    //get media
    @GET(GBGETMEDIA)
    Call<ResponseGbifMedia> getMedia(@Path(KEY) String key,@Query(LIMIT) int limit,@Query(OFFSET) int offset);

    //get
    @GET(GBGET)
    Call<ResponseSpecie> getGbif(@Path(KEY) String key);

    //test Data
    @GET("v1/species/search")
    Call<ResponseSetData> searchGbif(@Query(RANK) String rank, @Query(LIMIT) int limit, @Query(OFFSET) int offset);

    //Rss
    @GET("tin-moi-nhat.rss")
    Call<RssFeed> getFeed();
}
