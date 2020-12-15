package com.example.botanicallibrary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibraryFragment extends Fragment {
    private LibraryAdapter libraryAdapter;
    private GridLayoutManager gridLayoutManager;
    private Spinner spinnerFilt;
    private boolean isLoadingNext;
    private List<QueryDocumentSnapshot> documentSnapshots;

    protected LoadingDialog loadingDialog;
    protected int VISIBLETHRESHOLD=1, SIZEDATA=50;
    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isLoadingNext=false;
        documentSnapshots= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_library, container, false);
        String[] filtDatta = new String[]{getContext().getString(R.string.Class).toUpperCase()
                , getContext().getString(R.string.Phylum).toUpperCase()
                , getContext().getString(R.string.Family).toUpperCase()
                , getContext().getString(R.string.Order).toUpperCase()
                , getContext().getString(R.string.Genus).toUpperCase()
                , getContext().getString(R.string.Species).toUpperCase()};
        //spinner
        spinnerFilt = v.findViewById(R.id.Spinner_filt);
        ArrayAdapter<String> filtAdapter=new ArrayAdapter<>(v.getContext(),R.layout.support_simple_spinner_dropdown_item, filtDatta);
        filtAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFilt.setAdapter(filtAdapter);

        //list botanical
        RecyclerView recyclerView = v.findViewById(R.id.RvLibrary);
        loadingDialog=new LoadingDialog(getActivity());

        libraryAdapter=new LibraryAdapter(recyclerView,getContext(),documentSnapshots);
        gridLayoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem=gridLayoutManager.findLastVisibleItemPosition();
                if(documentSnapshots.size()<SIZEDATA) return;
                if(isLoadingNext && lastVisibleItem+ VISIBLETHRESHOLD >= documentSnapshots.size()){
                    loadingDialog.startDialog();
                    isLoadingNext=!isLoadingNext;
                    onLoadMore(spinnerFilt.getSelectedItem().toString()
                            ,SIZEDATA,
                            documentSnapshots.get(lastVisibleItem-1));
                    recyclerView.smoothScrollToPosition(0);
                }

            }
        });
        spinnerFilt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadingDialog.startDialog();
                onLoadMore(spinnerFilt.getSelectedItem().toString()
                        ,SIZEDATA,
                        null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }
    public void onLoadMore(String rank,int limit,DocumentSnapshot startOffset) {
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        CollectionReference colRef=ff.collection("Botanicals");
        if(startOffset!=null) colRef.startAfter(startOffset);
        colRef.limit(limit)
                .whereEqualTo("rank",rank)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        documentSnapshots.clear();
                        libraryAdapter.notifyItemRemoved(documentSnapshots.size());
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        documentSnapshots.add(null);
                        if(documentSnapshots.size()<SIZEDATA) isLoadingNext=false;
                        if(documentSnapshots.size()==0) return;
                        libraryAdapter.notifyDataSetChanged();
                        loadingDialog.dismissDialog();
                    }
                    else {
                        System.out.println("Error getting documents: "+task.getException());
                    }
                });
    }


}