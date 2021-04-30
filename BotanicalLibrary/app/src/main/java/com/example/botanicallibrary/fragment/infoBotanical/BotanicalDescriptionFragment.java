package com.example.botanicallibrary.fragment.infoBotanical;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.botanicallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BotanicalDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "key",COLLECTION="Botanicals",URL="url",DEFAULT="default";

    private String key;

    public BotanicalDescriptionFragment() {
        // Required empty public constructor
    }

    public static BotanicalDescriptionFragment newInstance(String param1) {
        BotanicalDescriptionFragment fragment = new BotanicalDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_botanical_description, container, false);
        WebView webView=v.findViewById(R.id.wiki);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(COLLECTION)
                .document(key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            webView.setWebViewClient(new WebViewClient());
                            if(task.getResult().get(URL)==null) webView.loadUrl("https://vi.wikipedia.org/wiki/"+task.getResult().get(DEFAULT));
                            else webView.loadUrl((String) task.getResult().get(URL));
                        }
                    }
                });

        return v;
    }
}