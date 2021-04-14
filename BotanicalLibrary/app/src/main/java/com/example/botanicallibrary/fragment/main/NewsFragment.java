package com.example.botanicallibrary.fragment.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botanicallibrary.Interface.ItemClickListener;
import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.NewsActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Rss.RssFeed;
import com.example.botanicallibrary.en.Rss.RssItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private class NewsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;
        private RecyclerView recyclerView;
        private List<RssItem> itemViewModels;

        public NewsAdapter(Context mContext, RecyclerView recyclerView, List<RssItem> itemViewModels) {
            this.mContext = mContext;
            this.recyclerView = recyclerView;
            this.itemViewModels = itemViewModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(mContext);
            View v=layoutInflater.inflate(R.layout.layout_news_item,parent,false);
            return new ViewHolder(v);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.name.setText(itemViewModels.get(position).getTitle());
            holder.date.setText(itemViewModels.get(position).getPubDate());
            Picasso.with(mContext).load(itemViewModels.get(position).getImage()).placeholder(R.drawable.icon_add_image_96).fit().centerCrop().into(holder.imageView);
            if(position%2==0) holder.ll_layout.setBackgroundColor(R.color.gray_400);
            holder.setItemClickListener((view, position1, isLongClick) -> {
                if(itemViewModels.size()>1) {
                    String url = itemViewModels.get(position1).getLink();
                    if(url==null) return;
                    Intent intent = new Intent(view.getContext(), NewsActivity.class);
                    intent.putExtra("URL", url);
                    view.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemViewModels.size();
        }
    }
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView imageView;
        private TextView name, date;
        private LinearLayout ll_layout;

        private ItemClickListener itemClickListener;

        public ItemClickListener getItemClickListener() {
            return itemClickListener;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.img_new);
            this.name=itemView.findViewById(R.id.name_news);
            this.date=itemView.findViewById(R.id.time_news);
            this.ll_layout=itemView.findViewById(R.id.ll_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return false;
        }
    }
}