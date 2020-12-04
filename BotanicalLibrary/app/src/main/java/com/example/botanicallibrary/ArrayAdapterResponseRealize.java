package com.example.botanicallibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.response.ResultRealizePlant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArrayAdapterResponseRealize extends ArrayAdapter<DataListViewResponseRealize> {
    public ArrayAdapterResponseRealize(@NonNull Context context, int resource, @NonNull List<DataListViewResponseRealize> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            convertView=layoutInflater.inflate(R.layout.activity_card_response_realize,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=convertView.findViewById(R.id.iVImageResponse);
            viewHolder.tVName=convertView.findViewById(R.id.tVName);
            viewHolder.tVScore=convertView.findViewById(R.id.tVScore);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        DataListViewResponseRealize resultRealizePlant=getItem(position);
        if(resultRealizePlant!=null){
            viewHolder.tVScore.setText("Score: "+resultRealizePlant.getScore().toString());
            viewHolder.tVName.setText(resultRealizePlant.getName());
            if(resultRealizePlant.getUrl()!=null) Picasso.with(getContext()).load(resultRealizePlant.getUrl()).fit().centerCrop().into(viewHolder.imageView);
        }
        return convertView;
    }
    private class ViewHolder{
        public ImageView imageView;
        public TextView tVName,tVScore;
    }

}
