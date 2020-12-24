package com.example.botanicallibrary.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botanicallibrary.BitanicalDetailActivity;
import com.example.botanicallibrary.Interface.ItemClickListener;
import com.example.botanicallibrary.NewsActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Rss.RssItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
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
        return new NewsAdapter.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(itemViewModels.get(position).getTitle());
        holder.date.setText(itemViewModels.get(position).getPubDate());
        Picasso.with(mContext).load(itemViewModels.get(position).getImage()).placeholder(R.drawable.icon_add_image_96).fit().centerCrop().into(holder.imageView);
        if(position%2==0) holder.ll_layout.setBackgroundColor(R.color.gray_400);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(itemViewModels.size()>1) {
                    String url = itemViewModels.get(position).getLink();
                    if(url==null) return;
                    Intent intent = new Intent(view.getContext(), NewsActivity.class);
                    intent.putExtra("URL", url);
                    view.getContext().startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemViewModels.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
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
