package com.example.botanicallibrary.en;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class PlantPost {
    public String organ;

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public Uri getFile() {
        return uri;
    }

    public void setFile(Uri uri) {
        this.uri = uri;
    }

    public Bitmap getBitmapImg() {
        return bitmapImg;
    }

    public void setBitmapImg(Bitmap bitmapImg) {
        this.bitmapImg = bitmapImg;
    }

    public Uri uri;
    public Bitmap bitmapImg;

    public PlantPost(String organ, Uri uri, Bitmap bitmapImg) {
        this.organ = organ;
        this.uri = uri;
        this.bitmapImg = bitmapImg;
    }
}
