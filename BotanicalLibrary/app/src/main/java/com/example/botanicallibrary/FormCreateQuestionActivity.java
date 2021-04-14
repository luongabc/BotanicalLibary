package com.example.botanicallibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCreateQuestionActivity extends AppCompatActivity {
    private ImageView iv_addImage;
    private String currentPhotoPath="";
    private Bitmap bitmap=null;
    private String email;
    private FirebaseFirestore db;
    private int i=0;
    private final LoadingDialog loadingDialog=new LoadingDialog(this);
    private StorageReference mStorageRef;


    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_create_question);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        SharedPreferences sp1=this.getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, MODE_PRIVATE);
        email=sp1.getString(Local.DeviceLocal.EMAIL, null);

        EditText edt_Name=findViewById(R.id.edt_Name);
        EditText edt_description=findViewById(R.id.edt_description);
        ImageView iv_done=findViewById(R.id.iv_done);
        iv_addImage=findViewById(R.id.iv_addImage);
        iv_addImage.setOnClickListener(v->{
            Intent intent1 = new Intent(getBaseContext(), SelectImage.class);
            Bundle bundle=new Bundle();
            bundle.putInt(Local.BundleLocal.WIDTH,0);
            bundle.putInt(Local.BundleLocal.QUALITY,100);
            intent1.putExtras(bundle);
            startActivityForResult(intent1, Local.REQUEST_CODE_GET_IMAGE);
        });

        iv_done.setOnClickListener(v->{
            if(edt_description.getText().toString().equals("")
                    ||edt_Name.getText().toString().equals("")
                    ||currentPhotoPath.equals("")
                    || bitmap==null ) return;
            loadingDialog.startDialog("Post question, wait...");
            Uri file = Uri.fromFile(new File(currentPhotoPath));
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss").format(new Date());
            String dir=Local.firebaseLocal.DIRIMAGE.concat(email).concat(timeStamp).concat(edt_Name.getText().toString()).concat(".image.jpg");
            StorageReference riversRef = mStorageRef.child(dir);

            riversRef.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> {
                        i++;
                        if(i==2) {
                            i=0;
                            loadingDialog.dismissDialog();
                            finish();
                        }
                    })
                    .addOnFailureListener(exception -> {
                        i++;
                        if(i==2) {
                            i=0;
                            loadingDialog.dismissDialog();
                        }
                    });
            Map<String, String> data=new HashMap<>();
            data.put(Local.firebaseLocal.IMAGE,dir);
            data.put(Local.firebaseLocal.DATE,timeStamp);
            data.put(Local.firebaseLocal.USER,email);
            data.put(Local.firebaseLocal.LABLE, edt_Name.getText().toString());
            data.put(Local.firebaseLocal.DESCRIPTION,edt_description.getText().toString());
            db.collection(Local.firebaseLocal.QUESTIONS)
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        i++;
                        if(i==2) {
                            i=0;
                            loadingDialog.dismissDialog();
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getBaseContext(),"Có lỗi, hãy thử lại",Toast.LENGTH_LONG);
                        i++;
                        if(i==2) {
                            i=0;
                            loadingDialog.dismissDialog();
                        }
                    });

        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Local.REQUEST_CODE_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (Objects.requireNonNull(data.getExtras()).get(Local.BundleLocal.PATHIMAGE)!=null) {

                currentPhotoPath = (String) data.getExtras().get(Local.BundleLocal.PATHIMAGE);
                try {
                    bitmap=BitmapFactory.decodeFile(currentPhotoPath);
                    iv_addImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}