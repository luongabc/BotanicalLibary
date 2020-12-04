package com.example.botanicallibrary.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.botanicallibrary.ListArrayAdapter;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.SelectImage;
import com.example.botanicallibrary.bl.Permission;
import com.example.botanicallibrary.bl.RetrofitService;
import com.example.botanicallibrary.en.PlantPost;

import java.util.ArrayList;

public class RealizePlantFragment extends Fragment {
    private final int REQUEST_CODE_GET_IMAGE=1;

    private ArrayList<PlantPost> plantPosts;
    private ListArrayAdapter plantPostListArrayAdapter;

    public RealizePlantFragment() {
        // Required empty public constructor
    }

    public static RealizePlantFragment newInstance() {
        RealizePlantFragment fragment = new RealizePlantFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent=new Intent(getContext(), SelectImage.class);
        startActivityForResult(intent,REQUEST_CODE_GET_IMAGE);

        View view=inflater.inflate(R.layout.fragment_realize_plant, container, false);
        ListView listViewPlantPost = view.findViewById(R.id.ListImg);
        ImageButton btnAddImage = view.findViewById(R.id.addImage);
        Button btnRealize = view.findViewById(R.id.btn_search_plant);
        plantPosts=new ArrayList<>();

        plantPostListArrayAdapter=new ListArrayAdapter(getContext(),R.layout.activity_card_plant, plantPosts);
        listViewPlantPost.setAdapter(plantPostListArrayAdapter);

        btnRealize.setOnClickListener(v -> {
            if(plantPosts.size()!=0 && checkNullListPost()){
                Permission.checkPermissionInteret(getActivity());
                btnRealize.setVisibility(View.GONE);
                btnAddImage.setVisibility(View.GONE);
                RetrofitService.RealizePlant(getContext(),plantPosts);
            }
            else {
                Toast.makeText(getContext(),"add image and organ",Toast.LENGTH_LONG).show();

            }
        });
        btnAddImage.setOnClickListener(v -> {
            Permission.checkPermissionCamera(getActivity());
            Permission.checkPermissionREAD_EXTERNAL_STORAGE(getActivity());
            Permission.checkPermissionWriteStorage(getActivity());
            if(plantPosts.size()<5) {
                Intent intent1 = new Intent(getContext(), SelectImage.class);
                startActivityForResult(intent1, REQUEST_CODE_GET_IMAGE);
            }
        });
        return view;
    }
    public boolean checkNullListPost(){
        for(int i=0;i<plantPosts.size();i++){
            if(plantPosts.get(i).uri==null ||
                plantPosts.get(i).getOrgan()==null||
                plantPosts.get(i).getBitmapImg()==null) return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_GET_IMAGE && resultCode==Activity.RESULT_OK && data.getExtras().get("pathImage")!=null) {
            String currentPhotoPath= (String) data.getExtras().get("pathImage");
            try {
                PlantPost plantPost=new PlantPost("flower",Uri.parse(currentPhotoPath), BitmapFactory.decodeFile(currentPhotoPath));
                plantPosts.add(plantPost);
                plantPostListArrayAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}