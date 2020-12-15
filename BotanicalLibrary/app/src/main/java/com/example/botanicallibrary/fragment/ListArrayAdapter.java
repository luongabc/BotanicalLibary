package com.example.botanicallibrary.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.PlantPost;

import java.util.List;

public class ListArrayAdapter extends ArrayAdapter<PlantPost> {
    public ListArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ListArrayAdapter(@NonNull Context context, int resource, @NonNull List<PlantPost> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder v;
        if(convertView==null){
            LayoutInflater li;
            li=LayoutInflater.from(getContext());
            convertView=li.inflate(R.layout.layout_card_plant,null);
            v=new ViewHolder() ;

            v.imageView=convertView.findViewById(R.id.imageView2);

            v.btnBark=convertView.findViewById(R.id.Bark);
            v.btnFlower=convertView.findViewById(R.id.Flower);
            v.btnFruit=convertView.findViewById(R.id.Fruit);
            v.btnLeaf=convertView.findViewById(R.id.Leaf);
            v.btnDelete=convertView.findViewById(R.id.btnDelete);
            convertView.setTag(v);
        }
        else {
            v= (ViewHolder) convertView.getTag();
        }
        PlantPost plantPost=getItem(position);
        if(plantPost!=null){
            v.imageView.setImageBitmap(plantPost.bitmapImg);
            Button[] btnOrgan=new Button[4];
            btnOrgan[0]=v.btnBark;
            btnOrgan[1]=v.btnFlower;
            btnOrgan[2]=v.btnFruit;
            btnOrgan[3]=v.btnLeaf;

            for (Button button : btnOrgan) {
                button.setOnClickListener(v12 -> {
                    for (int i1 = 0; i1 < 4; i1++) {
                        btnOrgan[i1].setBackgroundTintList(getContext().getColorStateList(R.color.grey));
                    }
                    v12.setBackgroundTintList(getContext().getColorStateList(R.color.purple_700));
                    plantPost.setOrgan(((Button) v12).getText().toString().toLowerCase());
                });
            }
            v.btnDelete.setOnClickListener(v1 -> {
                remove(plantPost);
                notifyDataSetChanged();
            });
        }
        return convertView;
    }
    private class ViewHolder {
        public Button btnBark,btnFlower,btnFruit,btnLeaf;
        public ImageView imageView,btnDelete;

    }
}
