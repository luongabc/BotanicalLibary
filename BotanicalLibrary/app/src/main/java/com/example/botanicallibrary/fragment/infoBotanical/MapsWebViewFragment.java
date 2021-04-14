package com.example.botanicallibrary.fragment.infoBotanical;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.bl.WeAppInterface;
import com.example.botanicallibrary.en.Local;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LoadingDialog loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.startDialog(getString(R.string.loading));
        ViewGroup v= requireActivity().findViewById(android.R.id.content);
        v.setClickable(false);
        View view= inflater.inflate(R.layout.fragment_maps_web_view, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Local.firebaseLocal.BOTANICALS)
                .document(keyBotanical)
                .collection(Local.firebaseLocal.AMOUNT)
                .get()
                .addOnCompleteListener(task -> {
                    String data = null;
                    if (task.isSuccessful()) {
                        data="";
                        Gson gson=new Gson();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            data=data.concat(gson.toJson(document.getData())).concat(",");
                        }
                        data=("[").concat(data).concat(",]");
                        data=data.replace(",,","");
                    }
                    else Toasty.info(requireContext(),"Không có dữ liệu",Toasty.LENGTH_SHORT).show();
                    WebView webView=view.findViewById(R.id.webView);
                    if(data!=null){
                        WeAppInterface weAppInterface =new WeAppInterface();
                        weAppInterface.setKey(data);
                        webView.addJavascriptInterface(weAppInterface,"Android");
                    }
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("file:///android_asset/leaflet/leaflet.html");
                    loadingDialog.dismissDialog();
                    v.setClickable(true);
                });
        return view;
    }
}