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
import androidx.core.content.FileProvider;

import com.example.botanicallibrary.bl.FileUtils;
import com.example.botanicallibrary.bl.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SelectImage extends Activity {
    private final int REQUEST_CODE_CAMERA=51   ,REQUEST_CODE_GALLERY=19;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        ImageButton btnCamera = findViewById(R.id.camera);
        ImageButton btnGallery = findViewById(R.id.gallery);

        btnCamera.setOnClickListener(v -> dispatchTakePictureIntent());
        btnGallery.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Select picture"),REQUEST_CODE_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAMERA && resultCode==Activity.RESULT_OK){
            int imageWidth = 720;
            int imageHeigth = 1028;
            setPic(imageWidth, imageHeigth);
            galleryAddPic();
        }
        else if(requestCode==REQUEST_CODE_GALLERY && resultCode==Activity.RESULT_OK){
            currentPhotoPath=(new FileUtils(getBaseContext())).getPath(data.getData());
            int imageWidth = 720;
            int imageHeigth = 1028;
            setPic(imageWidth, imageHeigth);

        }
        finish();
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra("pathImage",currentPhotoPath);
        setResult(RESULT_OK,intent);
        super.finish();
    }
    private void dispatchTakePictureIntent() {
        Permission.checkPermissionCamera(this);
        Permission.checkPermissionReadStorage(this);
        Permission.checkPermissionWriteStorage(this);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile =new File(createImageFile());
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
    private String createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return currentPhotoPath;
    }
    private void setPic(int width, int heigth) {
        Bitmap filebBitmap=BitmapFactory.decodeFile(currentPhotoPath);

        if(filebBitmap==null){
            System.out.println("nulllllllllllllllll"+currentPhotoPath);
            return;
        }
        Bitmap bitmap = Bitmap.createScaledBitmap(filebBitmap,width,heigth,true);
        try{
            File smallImage=new     File(createImageFile());
            FileOutputStream fileOutputStream = new FileOutputStream(smallImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        }catch (Exception ignored){

        }
    }
}