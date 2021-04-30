package com.example.botanicallibrary.fragment.responseRealize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class ArrayAdapterResponseRealize extends ArrayAdapter<DataListViewResponseRealize> {
    public ArrayAdapterResponseRealize(@NonNull Context context, int resource, @NonNull List<DataListViewResponseRealize> objects) {
        super(context, resource, objects);
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            convertView=layoutInflater.inflate(R.layout.layout_card_response_realize,null);
            viewHolder= new ViewHolder();
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
            DecimalFormat df = new DecimalFormat("#.####");
            viewHolder.tVScore.setText("Tỉ lệ: "+ df.format(Float.parseFloat(resultRealizePlant.getScore())*100)+"%" );
            viewHolder.tVName.setText(resultRealizePlant.getName());

            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection(Local.BOTANICALS)
                    .document(resultRealizePlant.getGbif())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            String name= (String) Objects.requireNonNull(task.getResult()).get(Local.NAME);
                            String urlImage= (String) Objects.requireNonNull(task.getResult()).get(Local.firebaseLocal.IMAGEBG);
                            if(name!=null && !name.equals("")) viewHolder.tVName.setText(name);
                            if(urlImage!=null && !urlImage.equals("")) {
                                Picasso.with(getContext()).load(urlImage).fit().centerCrop().into(viewHolder.imageView,new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {}
                                    @Override
                                    public void onError() {
                                        Picasso.with(getContext()).load(R.drawable.icons8_photo_gallery_100).into(viewHolder.imageView);
                                    }
                                });
                            }
                        }
                    });
        }
        return convertView;
    }


    private static class ViewHolder{
        public ImageView imageView;
        public TextView tVName,tVScore;
    }

}
