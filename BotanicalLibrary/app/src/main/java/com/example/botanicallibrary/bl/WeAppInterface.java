package com.example.botanicallibrary.bl;

import android.webkit.JavascriptInterface;

public class WeAppInterface {
    private String key;

    @JavascriptInterface
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
