package com.example.botanicallibrary.fragment;
import com.example.botanicallibrary.bl.WeAppInterface;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.botanicallibrary.R;

public class MapsWebViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "key";

    private String keyBotanical;

    public MapsWebViewFragment() {
        // Required empty public constructor
    }
    public static MapsWebViewFragment newInstance(Bundle param1) {
        MapsWebViewFragment fragment = new MapsWebViewFragment();
        fragment.setArguments(param1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyBotanical = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println(keyBotanical);
        View view= inflater.inflate(R.layout.fragment_maps_web_view, container, false);
        WebView webView=view.findViewById(R.id.webView);
        WeAppInterface weAppInterface =new WeAppInterface();
        weAppInterface.setKey(String.valueOf(keyBotanical));
        webView.addJavascriptInterface(weAppInterface,"Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/Maps.html");
        return view;
    }
}