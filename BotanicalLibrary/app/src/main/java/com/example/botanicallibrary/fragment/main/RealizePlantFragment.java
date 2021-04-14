package com.example.botanicallibrary.fragment.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.bl.Data;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.ResponseRealizeActivity;
import com.example.botanicallibrary.SelectImage;
import com.example.botanicallibrary.en.DataListViewResponseRealize;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.en.PlantPost;
import com.example.botanicallibrary.en.response.ResponseDataPost;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RealizePlantFragment extends Fragment {
    private LoadingDialog loadingDialog;
    private ArrayList<PlantPost> plantPosts;
    private ListArrayAdapter plantPostListArrayAdapter;
    private Intent intent1;
    public RealizePlantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intent1 = new Intent(getContext(), SelectImage.class);
        Bundle bundle=new Bundle();
        bundle.putInt(Local.BundleLocal.WIDTH,0);
        bundle.putInt(Local.BundleLocal.QUALITY,100);
        intent1.putExtras(bundle);
        startActivityForResult(intent1, Local.REQUEST_CODE_GET_IMAGE);

        View view=inflater.inflate(R.layout.fragment_realize_plant, container, false);
        ListView listViewPlantPost = view.findViewById(R.id.ListImg);
        ImageButton btnAddImage = view.findViewById(R.id.addImage);
        Button btnRealize = view.findViewById(R.id.btn_search_plant);
        plantPosts=new ArrayList<>();

        loadingDialog=new LoadingDialog(getActivity());
        plantPostListArrayAdapter= new ListArrayAdapter(requireContext(), R.layout.layout_card_plant, plantPosts);
        listViewPlantPost.setAdapter(plantPostListArrayAdapter);

        btnRealize.setOnClickListener(v -> {
            if(plantPosts.size()!=0 && checkNullListPost()){
                loadingDialog.startDialog(getString(R.string.loading));
                RealizePlant(plantPosts);
            }
            else {
                Toasty.warning(requireContext(), getString(R.string.tooLestInfo), Toast.LENGTH_LONG, true).show();
            }
        });
        btnAddImage.setOnClickListener(v -> {
            if(plantPosts.size()<5) {
                startActivityForResult(intent1, Local.REQUEST_CODE_GET_IMAGE);
            }
            else Toasty.warning(requireContext(), getString(R.string.max5), Toast.LENGTH_LONG, true).show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Local.REQUEST_CODE_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            if (Objects.requireNonNull(data.getExtras()).get(Local.BundleLocal.PATHIMAGE)!=null) {
                String currentPhotoPath = (String) data.getExtras().get(Local.BundleLocal.PATHIMAGE);
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
            File file=new  File(String.valueOf(plantPosts.get(i).getFile()));
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
            @Override
            public void onResponse(@NotNull Call<ResponseDataPost> call, @NotNull Response<ResponseDataPost> response) {
                if (!response.isSuccessful()) {
                    Toasty.error(requireContext(), getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                    loadingDialog.dismissDialog();
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
                bundle.putSerializable(Local.BundleLocal.RESPONSEREALIZE, (Serializable) dataListViewResponseRealizes);
                intent.putExtras(bundle);
                startActivity(intent);
                loadingDialog.dismissDialog();
            }
            @Override
            public void onFailure(@NotNull Call<ResponseDataPost> call, @NotNull Throwable t) {
                loadingDialog.dismissDialog();
                Toasty.error(requireContext(),  getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
    private static class ListArrayAdapter extends ArrayAdapter<PlantPost> {

        public ListArrayAdapter(@NonNull Context context, int resource, @NonNull List<PlantPost> objects) {
            super(context, resource, objects);
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder v;
            if(convertView==null){
                LayoutInflater li;
                li=LayoutInflater.from(getContext());
                convertView=li.inflate(R.layout.layout_card_plant,null);
                v= new ViewHolder();

                v.imageView=convertView.findViewById(R.id.imageView2);

                v.btnBark=convertView.findViewById(R.id.Bark);
                v.btnFlower=convertView.findViewById(R.id.Flower);
                v.btnFruit=convertView.findViewById(R.id.Fruit);
                v.btnLeaf=convertView.findViewById(R.id.Leaf);
                v.btnDelete=convertView.findViewById(R.id.btnDelete);
                convertView.setTag(v);
            }
            else {
                v= (ViewHolder) convertView.getTag();
            }
            PlantPost plantPost=getItem(position);
            if(plantPost!=null){
                v.imageView.setImageBitmap(plantPost.bitmapImg);
                Button[] btnOrgan=new Button[4];
                btnOrgan[0]=v.btnBark;
                btnOrgan[1]=v.btnFlower;
                btnOrgan[2]=v.btnFruit;
                btnOrgan[3]=v.btnLeaf;

                for (Button button : btnOrgan) {
                    button.setOnClickListener(v12 -> {
                        for (int i1 = 0; i1 < 4; i1++) {
                            btnOrgan[i1].setBackgroundTintList(getContext().getColorStateList(R.color.grey));
                        }
                        v12.setBackgroundTintList(getContext().getColorStateList(R.color.purple_700));
                        plantPost.setOrgan(((Button) v12).getText().toString().toLowerCase());
                    });
                }
                v.btnDelete.setOnClickListener(v1 -> {
                    remove(plantPost);
                    notifyDataSetChanged();
                });
            }
            return convertView;
        }
        private static class ViewHolder {
            public Button btnBark,btnFlower,btnFruit,btnLeaf;
            public ImageView imageView,btnDelete;

        }
    }
}