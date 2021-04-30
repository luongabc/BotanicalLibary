package com.example.botanicallibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChangeDetailActivity extends AppCompatActivity {
    private String key;
    private String keyUser;
    protected Activity activity=this;
    private List<DataAdapter> dataAdapters;
    private LinearLayout linearLayout;
    private ImageView iv_offsetImageSelect;
    private int offsetSelect=0;
    private int numLoad=0;
    private Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        key= (String) getIntent().getExtras().get(Local.BundleLocal.KEY);
        setContentView(R.layout.activity_change_detail);
        linearLayout=findViewById(R.id.ll_layout);

        SharedPreferences sp1=getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
        keyUser=sp1.getString(Local.DeviceLocal.ID,null);

        intent1 = new Intent(getBaseContext(), SelectImage.class);
        Bundle bundle=new Bundle();
        bundle.putInt(Local.BundleLocal.WIDTH,0);
        bundle.putInt(Local.BundleLocal.QUALITY,100);
        intent1.putExtras(bundle);

        dataAdapters=new ArrayList<>();
        ImageView btnAdd=findViewById(R.id.iv_newDescription);
        btnAdd.setOnClickListener(v -> {
            for(int i=0;i<dataAdapters.size();i++){
                if(dataAdapters.get(i).name==null || dataAdapters.get(i).name.equals("")) return;
            }
            dataAdapters.add(new DataAdapter("", "", ""));
            addContentLayout(dataAdapters.get(dataAdapters.size()-1));
        });
        ImageView  btn_send=findViewById(R.id.iv_push);

        btn_send.setOnClickListener(v -> {
            LoadingDialog loadingDialog=new LoadingDialog(this);
            loadingDialog.startDialog(getString(R.string.loading));
            for(int i=0;i<dataAdapters.size();i++){
                if(dataAdapters.get(i).urlImg==null
                    ||dataAdapters.get(i).urlImg.equals("")) break;
                File fileImage=new File(dataAdapters.get(i).urlImg);
                if(fileImage.exists() && !fileImage.isDirectory()){
                    Uri uri = Uri.fromFile(new File(dataAdapters.get(i).urlImg));
                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(Local.FOMATDATE).format(new Date());
                    dataAdapters.get(i).urlImg=Local.firebaseLocal.DIRIMAGE.concat(keyUser).concat(timeStamp).concat(dataAdapters.get(i).name).concat(".image.jpg");
                    StorageReference riversRef = mStorageRef.child(dataAdapters.get(i).urlImg);
                    riversRef.putFile(uri)
                            .addOnSuccessListener(t -> {
                            })
                            .addOnFailureListener(e -> {
                                loadingDialog.dismissDialog();
                                Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                            });
                }
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for(int i=0;i<dataAdapters.size();i++) {
                db.collection(Local.firebaseLocal.CONTRIBUTE)
                        .document(key)
                        .collection(keyUser)
                        .document(String.valueOf(i))
                        .set(dataAdapters.get(i))
                        .addOnSuccessListener(documentReference -> {
                            loadingDialog.dismissDialog();
                            Toasty.success(getBaseContext(), getString(R.string.Success), Toast.LENGTH_SHORT, true).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            loadingDialog.dismissDialog();
                            Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                        });
            }
        });
        this.getData(key);
    }
    private void addContentLayout(DataAdapter adapter){
        int offset=dataAdapters.size()-1;
        LayoutInflater layoutInflater= LayoutInflater.from(getBaseContext());
        @SuppressLint("InflateParams") ViewGroup viewGroup= (ViewGroup) layoutInflater.inflate(R.layout.layout_card_description,null);

        EditText editName=viewGroup.findViewById(R.id.name);
        EditText editContent=viewGroup.findViewById(R.id.content);
        ImageView iv_add=viewGroup.findViewById(R.id.iv_description);
        ImageButton btn_delete=viewGroup.findViewById(R.id.btn_action);
        View view1 =viewGroup.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
        btn_delete.setVisibility(View.VISIBLE);
        editName.setText(adapter.name);
        editContent.setText(adapter.content);
        String url;
        if(adapter.getUrlImg() == null || adapter.urlImg.equals("") )  url=String.valueOf(R.drawable.icon_add_image_96);
        else url=adapter.urlImg;
        Picasso.with(this).load(url)
                .placeholder(R.drawable.icon_add_image_96)
                .centerInside()
                .fit()
                .into(iv_add);
        iv_add.setOnClickListener(v->{
            iv_offsetImageSelect=(ImageView) v;
            offsetSelect=offset;
            startActivityForResult(intent1, Local.REQUEST_CODE_GET_IMAGE);
        });
        btn_delete.setVisibility(View.VISIBLE);
        btn_delete.setOnClickListener(v -> {
            ((ViewGroup)viewGroup.getParent()).removeViewAt(offset);
            dataAdapters.remove(offset);
            if(offset<numLoad && numLoad>0) numLoad--;
        });
        Picasso.with(this).load(R.drawable.icons_delete_64)
                .centerInside()
                .fit()
                .into(btn_delete);
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")){
                    int offset=((ViewGroup)(viewGroup.getParent())).indexOfChild(viewGroup);
                    dataAdapters.get(offset).name= ""+ s;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        linearLayout.addView(viewGroup);
    }
    private void deleteDocuments(String key){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Local.firebaseLocal.BOTANICALS)
                .document(key)
                .collection(Local.firebaseLocal.DESCRIPTION)
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                        firebaseFirestore.collection(Local.firebaseLocal.BOTANICALS)
                                .document(key)
                                .collection(Local.firebaseLocal.DESCRIPTION)
                                .document(documentSnapshot.getId())
                                .delete();
                    }
                });
    }
    //private add
    private void getData(String key){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Local.firebaseLocal.BOTANICALS)
                .document(key)
                .collection(Local.firebaseLocal.DESCRIPTION)
                .get()
                .addOnCompleteListener(task -> {
                    numLoad= Objects.requireNonNull(task.getResult()).size();
                    for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                        String name=(String)documentSnapshot.get(Local.firebaseLocal.NAME);
                        String content=(String)documentSnapshot.get(Local.firebaseLocal.CONTENT);
                        String urlImg=(String)documentSnapshot.get(Local.firebaseLocal.IMAGE);
                        //String keyDescription=(String)documentSnapshot.getId();
                        DataAdapter dataAdapter= new DataAdapter(name, content, urlImg);
                        dataAdapters.add(dataAdapter);
                        addContentLayout(dataAdapter);
                    }
                });
    }
    protected static class DataAdapter{
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

        private String name,content,urlImg;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Local.REQUEST_CODE_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (Objects.requireNonNull(data.getExtras()).get(Local.BundleLocal.PATHIMAGE)!=null) {
                String currentPhotoPath = (String) data.getExtras().get(Local.BundleLocal.PATHIMAGE);
                dataAdapters.get(offsetSelect).urlImg= currentPhotoPath;
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    iv_offsetImageSelect.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
