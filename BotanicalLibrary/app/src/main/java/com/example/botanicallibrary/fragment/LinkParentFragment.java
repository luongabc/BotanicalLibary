package com.example.botanicallibrary.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.botanicallibrary.BitanicalDetailActivity;
import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.response.ResponseSpecie;
import com.example.botanicallibrary.en.response.demosetdata.ResponseSetData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinkParentFragment extends Fragment implements View.OnClickListener{
    private String key;
    private static final  String KEY="key", BOTANICALS="Botanicals",NAME="name",DEFAULT="default",RANK="rank",PARENTkEY="parentKey";

    public LinkParentFragment() {
        // Required empty public constructor
    }

    public static LinkParentFragment newInstance(Bundle bundle) {
        LinkParentFragment fragment = new LinkParentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()==null) return;
        key=getArguments().getString(KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_link_parent, container, false);
        LinearLayout listPrent=view.findViewById(R.id.ll_listParent);
        System.out.println(key);

        setParent(listPrent,key);

        return view;
    }
    private void setParent(@NonNull LinearLayout linearLayout,@NonNull String key){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(BOTANICALS)
                .document(key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String name= (String) task.getResult().get(NAME);
                            if(name==null) name=(String) task.getResult().get(DEFAULT);
                            String rank=(String) task.getResult().get(RANK);
                            TextView textView =new TextView(getContext());
                            textView.setText(rank+" : "+name+"  ->");
                            textView.setTextColor(R.color.light_blue_900);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =new Intent(getContext(),BitanicalDetailActivity.class);
                                    intent.putExtra(KEY,key);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                            linearLayout.addView(textView);
                            if(task.getResult().get(PARENTkEY)==null) return;
                            setParent(linearLayout,task.getResult().get(PARENTkEY).toString());
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        String key= (String) ((Button)v).getContentDescription();
        this.onDestroy();
        Intent intent=new Intent(getContext(),BitanicalDetailActivity.class);
        intent.putExtra(KEY   ,key);
        startActivity(intent);
    }
}