package com.example.botanicallibrary.fragment;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.bl.Data;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.ResponseRealizeActivity;
import com.example.botanicallibrary.SelectImage;
import com.example.botanicallibrary.bl.Permission;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.PlantPost;
import com.example.botanicallibrary.en.response.ResponseDataPost;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RealizePlantFragment extends Fragment {
    private final int REQUEST_CODE_GET_IMAGE=1;
    private LoadingDialog loadingDialog;
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

        loadingDialog=new LoadingDialog(getActivity());
        plantPostListArrayAdapter=new ListArrayAdapter(requireContext(),R.layout.layout_card_plant, plantPosts);
        listViewPlantPost.setAdapter(plantPostListArrayAdapter);

        btnRealize.setOnClickListener(v -> {
            if(plantPosts.size()!=0 && checkNullListPost()){
                loadingDialog.startDialog("Loading...");
                RealizePlant(plantPosts);
            }
            else {
                Toast.makeText(getContext(),"add image and organ",Toast.LENGTH_LONG).show();

            }
        });
        btnAddImage.setOnClickListener(v -> {
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
        if(requestCode == REQUEST_CODE_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (Objects.requireNonNull(data.getExtras()).get("pathImage")!=null) {
                String currentPhotoPath = (String) data.getExtras().get("pathImage");
                try {
                    PlantPost plantPost = new PlantPost("flower", Uri.parse(currentPhotoPath), BitmapFactory.decodeFile(currentPhotoPath));
                    plantPosts.add(plantPost);
                    plantPostListArrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
    protected void RealizePlant(ArrayList<PlantPost>  plantPosts){
        List<MultipartBody.Part> fileToUploads=new ArrayList<>();
        List<RequestBody> organs=new ArrayList<>();
        for(int i=0;i<plantPosts.size();i++){
            File file=new  File(Objects.requireNonNull(plantPosts.get(i).getFile().getPath()));
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            fileToUploads.add(MultipartBody.Part.createFormData("images", file.getName(), requestBody));

            organs.add(RequestBody.create(plantPosts.get(i).getOrgan(),MediaType.parse("multipart/form-data")));
        }
        RetrofitAPI getResponse =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.MY_API_PLANT)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitAPI.class);

        Call<ResponseDataPost> call;
        switch(plantPosts.size()){
            case 1 : call = getResponse.postImg(fileToUploads.get(0), organs.get(0));
                break;
            case 2: call = getResponse.postImgTwo(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1));
                break;
            case 3: call = getResponse.postImgThree(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2));
                break;
            case 4: call = getResponse.postImgFore(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2),fileToUploads.get(3), organs.get(3));
                break;
            case 5: call = getResponse.postImgFire(fileToUploads.get(0), organs.get(0),fileToUploads.get(1), organs.get(1),fileToUploads.get(2), organs.get(2),fileToUploads.get(3), organs.get(3),fileToUploads.get(4), organs.get(4));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + plantPosts.size());
        }

        assert call != null;
        call.enqueue(new Callback<ResponseDataPost>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NotNull Call<ResponseDataPost> call, @NotNull Response<ResponseDataPost> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Fail Get Data ", Toast.LENGTH_LONG).show();
                    return;
                }
                List<DataListViewResponseRealize> dataListViewResponseRealizes = new ArrayList<>();
                for (int i = 0; i < Objects.requireNonNull(response.body()).getResultRealizePlants().size(); i++) {
                    dataListViewResponseRealizes.add(new DataListViewResponseRealize(null
                            , response.body().getResultRealizePlants().get(i).getSpecies().getScientificNameWithoutAuthor()
                            , response.body().getResultRealizePlants().get(i).getScore().toString()
                            , response.body().getResultRealizePlants().get(i).getGbif().getId()));
                    Data.getSpecies(response.body().getResultRealizePlants().get(i).getGbif().getId());
                }
                Intent intent = new Intent(getContext(), ResponseRealizeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("responseRealize", (Serializable) dataListViewResponseRealizes);
                intent.putExtras(bundle);
                startActivity(intent);
                loadingDialog.dismissDialog();
            }
            @Override
            public void onFailure(@NotNull Call<ResponseDataPost> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}