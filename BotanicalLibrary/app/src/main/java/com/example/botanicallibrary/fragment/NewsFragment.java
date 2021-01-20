package com.example.botanicallibrary.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Rss.RssFeed;
import com.example.botanicallibrary.en.Rss.RssItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class NewsFragment extends Fragment {
    private LinearLayout llListNews;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<RssItem> rssItemList;
    public NewsFragment() {
    }
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_news, container, false);
        rssItemList=new ArrayList<>();
        newsAdapter=new NewsAdapter(getContext(),recyclerView,rssItemList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rv_listNews);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);
        GetNews();
        return view;
    }

    private void GetNews(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.RSSURL)
                .addConverterFactory(SimpleXmlConverterFactory.create());
        /*HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);
        builder.client(httpClient.build());*/

        Retrofit retrofit = builder.build();

        RetrofitAPI rssService = retrofit.create(RetrofitAPI.class);

        Call<RssFeed> callAsync = rssService.getFeed();

        callAsync.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    RssFeed apiResponse = response.body();
                    rssItemList.addAll(apiResponse.channel.item);
                    newsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(),"ERROR",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                if (call.isCanceled()) {
                    System.out.println("Call was cancelled forcefully");
                } else {
                    Toast.makeText(getContext(),"Network Error :: " + t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}