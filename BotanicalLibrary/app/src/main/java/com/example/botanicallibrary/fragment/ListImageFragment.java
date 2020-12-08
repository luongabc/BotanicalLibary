package com.example.botanicallibrary.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.botanicallibrary.Interface.RetrofitAPI;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.response.ResponseGbifMedia;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListImageFragment extends Fragment {

    private static final String KEY = "key";
    private String key;

    public ListImageFragment() {
        // Required empty public constructor
    }
    public static ListImageFragment newInstance(String param1) {
        ListImageFragment fragment = new ListImageFragment();
        Bundle args = new Bundle();
        args.putString(KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list_image, container, false);
        TableLayout tableLayout=view.findViewById(R.id.TLListImage);
        GetGbifMedia(key,20,0,3,tableLayout);
        return view;
    }
    protected void GetGbifMedia(String key, int limit, int offset,int sizeCols ,TableLayout tableLayout){
        if(sizeCols<1||limit<1) return;
        RetrofitAPI getGbif =new Retrofit.Builder()
                .baseUrl(RetrofitAPI.GBIF)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);
        Call<ResponseGbifMedia> call=getGbif.getMedia(key,limit,offset);
        call.enqueue(new Callback<ResponseGbifMedia>() {
            @Override
            public void onResponse(@NotNull Call<ResponseGbifMedia> call, @NotNull Response<ResponseGbifMedia> response) {
                if (!response.isSuccessful()) return;
                for (int i = 0; i < Objects.requireNonNull(response.body()).getResults().size() / sizeCols; i++) {
                    TableRow tableRow = new TableRow(tableLayout.getContext());
                    for (int col = 0; col < sizeCols; col++) {
                        if (i * sizeCols + col >= response.body().getResults().size()) return;
                        ImageView imageView = new ImageView(tableLayout.getContext());
                        Picasso.with(getContext())
                                .load(response.body().getResults().get(i * sizeCols + col).getIdentifier())
                                .placeholder(R.drawable.ic_launcher_background)
                                .resize(tableLayout.getWidth() / sizeCols, tableLayout.getWidth() / sizeCols)
                                .into(imageView);
                        tableRow.addView(imageView);
                    }
                    tableLayout.addView(tableRow);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseGbifMedia> call, @NotNull Throwable t) {
                System.out.println("Get Gbif Media Fail");
            }
        });
    }
}