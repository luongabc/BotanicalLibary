package com.example.botanicallibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.botanicallibrary.bl.FileUtils;
import com.example.botanicallibrary.bl.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SelectImage extends Activity {
    private final int REQUEST_CODE_CAMERA=51   ,REQUEST_CODE_GALLERY=19, IMGWIDTH=400, IMGHEIGTH=600;
    private String TYPEIMAGE="image/*",PATHIMAGE="pathImage";
    private String currentPhotoPath, imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        ImageButton btnCamera = findViewById(R.id.camera);
        ImageButton btnGallery = findViewById(R.id.gallery);
        btnCamera.setOnClickListener(v ->{
            if(!Permission.requestPermissionCamera(this)||
                    !Permission.checkPermissionReadStorage(this)||
                    !Permission.checkPermissionWriteStorage(this)) return;
            dispatchTakePictureIntent();
        });
        btnGallery.setOnClickListener(v -> {
            if(!Permission.requestPermissionCamera(this)||
                !Permission.checkPermissionReadStorage(this)||
                !Permission.checkPermissionWriteStorage(this)) return;
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT).setType(TYPEIMAGE);
            startActivityForResult(Intent.createChooser(intent,"Select picture"),REQUEST_CODE_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAMERA && resultCode==Activity.RESULT_OK){
            galleryAddPic();
            setPic(IMGWIDTH, IMGHEIGTH);
        }
        else if(requestCode==REQUEST_CODE_GALLERY && resultCode==Activity.RESULT_OK){
            currentPhotoPath=(new FileUtils(getBaseContext())).getPath(data.getData());
            setPic(IMGWIDTH, IMGHEIGTH);
        }
        finish();
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra(PATHIMAGE,currentPhotoPath);
        setResult(RESULT_OK,intent);
        super.finish();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile =new File(createImageFile(null));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private String createNameFile(String name){
        if(name==null){
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return "JPEG_" + timeStamp + "_";
        }
        return "SEND_"+name;
    }
    private String createImageFile(String name) throws IOException {
        imageFileName =this.createNameFile(name);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return currentPhotoPath;
    }

    private void setPic(int width, int heigth) {
        Bitmap filebBitmap=BitmapFactory.decodeFile(currentPhotoPath);
        if(filebBitmap==null) return;
        Bitmap bitmap = Bitmap.createScaledBitmap(filebBitmap,width,heigth,true);
        try{
            File smallImage=new     File(createImageFile(imageFileName));
            FileOutputStream fileOutputStream = new FileOutputStream(smallImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        }catch (Exception ignored){
            Toast.makeText(this,ignored.getMessage(),Toast.LENGTH_LONG);
        }
    }
}