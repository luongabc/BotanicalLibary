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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.canhub.cropper.CropImageView;
import com.example.botanicallibrary.bl.FileUtils;
import com.example.botanicallibrary.bl.Permission;
import com.example.botanicallibrary.en.Local;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class SelectImage extends Activity {
    private int  imgWidth, imgQuality ;
    private final String TYPEIMAGE="image/*";
    private String currentPhotoPath, imageFileName;
    private CropImageView cropImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        imgWidth=bundle.getInt(Local.BundleLocal.WIDTH);
        imgQuality=bundle.getInt(Local.BundleLocal.QUALITY);


        setContentView(R.layout.activity_select_image);
        cropImageView=findViewById(R.id.cropImageView);
        ImageView iv_ok=findViewById(R.id.iv_ok);
        ImageView iv_rotation=findViewById(R.id.iv_rotation);
        iv_rotation.setOnClickListener(v->{
            cropImageView.rotateImage(90);
        });
        iv_ok.setOnClickListener(v->{
            if(currentPhotoPath==null) finish();
            else {
                Bitmap cropped = cropImageView.getCroppedImage();
                try {
                    currentPhotoPath = createImageFile(null);
                    File smallImage = new File(currentPhotoPath);
                    FileOutputStream fileOutputStream = new FileOutputStream(smallImage);
                    cropped.compress(Bitmap.CompressFormat.JPEG, this.imgQuality, fileOutputStream);
                } catch (Exception e) {
                    Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_LONG, true).show();
                }

                finish();
            }
        });
        ImageButton btnCamera = findViewById(R.id.camera);
        ImageButton btnGallery = findViewById(R.id.gallery);
        btnCamera.setOnClickListener(v ->{
            if(!Permission.requestPermissionCamera(this)||
                    !Permission.checkPermissionReadStorage(this)||
                    !Permission.checkPermissionWriteStorage(this)) {
                Toasty.error(getBaseContext(), getString(R.string.dontHavePermissionCamera), Toast.LENGTH_SHORT, true).show();
                return;
            }
            dispatchTakePictureIntent();
        });
        btnGallery.setOnClickListener(v -> {
            if(!Permission.requestPermissionCamera(this)||
                !Permission.checkPermissionReadStorage(this)||
                !Permission.checkPermissionWriteStorage(this)) {
                Toasty.error(getBaseContext(), getString(R.string.dontHavePermissionReadStorage), Toast.LENGTH_SHORT, true).show();
                return;
            }
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT).setType(TYPEIMAGE);
            startActivityForResult(Intent.createChooser(intent,"Select picture"), Local.REQUEST_CODE_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Local.REQUEST_CODE_CAMERA && resultCode==Activity.RESULT_OK){
            galleryAddPic();
            cropImageView.setImageBitmap( BitmapFactory.decodeFile(currentPhotoPath));
        }
        else if(requestCode==Local.REQUEST_CODE_GALLERY && resultCode==Activity.RESULT_OK){
            currentPhotoPath=(new FileUtils(getBaseContext())).getPath(data.getData());
            cropImageView.setImageBitmap( BitmapFactory.decodeFile(currentPhotoPath));
        }

    }

    @Override
    public void finish() {
        if(this.imgWidth>0)setPic();
        Intent intent=new Intent();
        intent.putExtra(Local.BundleLocal.PATHIMAGE,currentPhotoPath);
        setResult(RESULT_OK,intent);
        super.finish();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                startActivityForResult(takePictureIntent, Local.REQUEST_CODE_CAMERA);
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
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(Local.FOMATDATE).format(new Date());
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

    @SuppressLint("ShowToast")
    private void setPic() {
        Bitmap filebBitmap=BitmapFactory.decodeFile(currentPhotoPath);
        if(filebBitmap==null) return;
        Bitmap bitmap = Bitmap.createScaledBitmap(filebBitmap,this.imgWidth,filebBitmap.getHeight()*this.imgWidth/filebBitmap.getWidth(),true);
        try{
            File smallImage=new     File(createImageFile(imageFileName));
            FileOutputStream fileOutputStream = new FileOutputStream(smallImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,this.imgQuality,fileOutputStream);
        }catch (Exception e){
            Toasty.error(getBaseContext(), getString(R.string.Error), Toast.LENGTH_LONG, true).show();
        }
    }
}