package com.example.botanicallibrary.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botanicallibrary.Interface.ItemClickListener;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public abstract   class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private final Context mContext;
    private final RecyclerView recyclerView;
    private final List<QueryDocumentSnapshot> itemViewModels;


    protected String out;

    public LibraryAdapter(RecyclerView recyclerView, Context mContext, List<QueryDocumentSnapshot> itemViewModels) {
        this.mContext = mContext;
        this.itemViewModels = itemViewModels;
        this.recyclerView = recyclerView;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.layout_library_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (itemViewModels.get(position) == null) {
            holder.textView.setText(null);
            holder.imageView.getLayoutParams().height = recyclerView.getWidth() / 2;
            holder.imageView.setImageDrawable(null);
            return;
        }
        String nameVi = (String) itemViewModels.get(position).get(Local.NAME);
        String url = (String) itemViewModels.get(position).get(Local.IMAGEBG);
        if (nameVi == null)
            nameVi = (String) itemViewModels.get(position).get(Local.NAMEDEFAULT);
        holder.textView.setText(nameVi);
        holder.imageView.getLayoutParams().height = recyclerView.getWidth() / 2;
        Picasso.with(mContext).load(url).fit().into(holder.imageView);
        String finalNameVi = nameVi;
        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (itemViewModels.size() > 1) {
                String key = itemViewModels.get(position1).getId();
                event_onclick(view.getContext(),key, finalNameVi,out);
            }
        });
    }
    protected abstract void event_onclick(Context context, String key, String name, String outPut);
    @Override
    public int getItemCount() {
        return itemViewModels.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView imageView;
        public TextView textView;

        public ItemClickListener getItemClickListener() {
            return itemClickListener;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }

}