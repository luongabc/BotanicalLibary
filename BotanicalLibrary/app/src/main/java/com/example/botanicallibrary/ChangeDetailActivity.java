package com.example.botanicallibrary;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.botanicallibrary.Interface.IDataFireBase;
import com.example.botanicallibrary.bl.Data;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

public class ChangeDetailActivity extends AppCompatActivity {
    private String key="1000002";
    protected Activity activity=this;
    private RecyclerView recyclerView;
    private List<DataAdapter> dataAdapters;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //key= (String) getIntent().getExtras().get("key");
        setContentView(R.layout.activity_change_detail);
        setTitle(getResources().getString(R.string.title_activity_change_bitanical));
        linearLayout=findViewById(R.id.ll_layout);

        dataAdapters=new ArrayList<>();
        ImageView btnAdd=findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataAdapters.add(new DataAdapter("","",""));
                addContentLayout(new DataAdapter("","",""));
            }
        });
        ImageView  btn_send=findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog=new LoadingDialog(activity);
                FirebaseFirestore ff=FirebaseFirestore.getInstance();
                deleteDocuments(key);
                for(int i=0; i<dataAdapters.size(); i++){
                    if(dataAdapters.get(i).name.equals("")|| dataAdapters.get(i).content.equals("")){
                        dataAdapters.remove(i);
                        i--;
                        continue;
                    }
                    Map<String, String > stringMap =new HashMap<>();
                    stringMap.put("name",dataAdapters.get(i).name);
                    stringMap.put("content",dataAdapters.get(i).content);
                    stringMap.put("urlImg","null");
                    ff.collection("Botanicals")
                            .document(key)
                            .collection("Descriptions")
                            .document(String.valueOf(i))
                            .set(stringMap, SetOptions.merge());
                }
            }
        });
        this.getData(key);

    }
    private void addContentLayout(DataAdapter dataAdapter){
        LayoutInflater layoutInflater= LayoutInflater.from(getBaseContext());
        final ViewGroup viewGroup= (ViewGroup) layoutInflater.inflate(R.layout.card_description_layout,null);
        EditText editName=viewGroup.findViewById(R.id.name);
        EditText editContent=viewGroup.findViewById(R.id.content);
        ImageView add=viewGroup.findViewById(R.id.img);
        ImageView btn_delete=viewGroup.findViewById(R.id.btn_delete);
        Picasso.with(getBaseContext())
                .load(R.drawable.icondelete_48)
                .into(btn_delete);
        editName.setText(dataAdapter.name);
        editContent.setText(dataAdapter.content);
        String url;
        if(dataAdapter.getUrlImg() == null || dataAdapter.urlImg.equals("") )  url=String.valueOf(R.drawable.icon_add_image_96);
        else url=dataAdapter.urlImg;
        Picasso.with(getBaseContext()).load(url)
                .placeholder(R.drawable.icon_add_image_96)
                .centerInside()
                .fit()
                .into(add);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offset=((ViewGroup)(viewGroup.getParent())).indexOfChild(viewGroup);
                ((ViewGroup)viewGroup.getParent()).removeViewAt(offset);
                dataAdapters.remove(offset);
            }
        });

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")){
                    int offset=((ViewGroup)(viewGroup.getParent())).indexOfChild(viewGroup);
                    dataAdapters.get(offset).content=""+s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")){
                    int offset=((ViewGroup)(viewGroup.getParent())).indexOfChild(viewGroup);
                    System.out.println(offset);
                    dataAdapters.get(offset).name= ""+ s;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(viewGroup);
    }
    private void deleteDocuments(String key){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Botanicals")
                .document(key)
                .collection("Descriptions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            firebaseFirestore.collection("Botanicals")
                                    .document(key)
                                    .collection("Descriptions")
                                    .document(documentSnapshot.getId())
                                    .delete();
                        }

                    }
                });
    }
    //private add
    private void getData(String key){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Botanicals")
                .document(key)
                .collection("Descriptions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String name=(String)documentSnapshot.get("name");
                            String content=(String)documentSnapshot.get("content");
                            String urlImg=(String)documentSnapshot.get("img");
                            String keyDescription=(String)documentSnapshot.getId();
                            DataAdapter dataAdapter=new DataAdapter(name,content,urlImg);
                            dataAdapters.add(dataAdapter);
                            addContentLayout(dataAdapter);
                        }

                    }
                });
    }
    protected class DataAdapter{
        public DataAdapter(String name, String content, String urlImg) {
            this.name = name;
            this.content = content;
            this.urlImg = urlImg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrlImg() {
            return urlImg;
        }

        public void setUrlImg(String urlImg) {
            this.urlImg = urlImg;
        }

        private String name,content,urlImg;
    }
}
