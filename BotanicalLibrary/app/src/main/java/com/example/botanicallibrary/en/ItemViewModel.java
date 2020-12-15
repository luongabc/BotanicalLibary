package com.example.botanicallibrary.en;

import android.content.ClipData.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel  {
    private String url,name,id;

    public String getUrl() {
        return url;
    }

    public ItemViewModel(String url, String name, String id) {
        this.url = url;
        this.name = name;
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
