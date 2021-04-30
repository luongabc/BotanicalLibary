package com.example.botanicallibrary.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botanicallibrary.BitanicalDetailActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.template.LibraryAdapter;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LibraryFragment extends Fragment {

    private AddapterSub addapterSub;
    private Spinner spinnerFilt;

    private List<QueryDocumentSnapshot> documentSnapshots;
    protected LoadingDialog loadingDialog;
    private static final String[] filtDatta = new String[]{Local.SPECIES, Local.GENUS, Local.FAMILY, Local.ORDER, Local.PHYLUM, Local.CLASS};
    private static final String[] filtDatta_vi = new String[]{"Loài", "Chi", "Họ", "Bộ", "Ngành","Lớp"};
    private boolean bl_loadmore=false;
    private String strFilt,strSearch;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        documentSnapshots = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        //spinner
        spinnerFilt = view.findViewById(R.id.Spinner_filt);
        ArrayAdapter<String> filtAdapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, filtDatta_vi);
        filtAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFilt.setAdapter(filtAdapter);
        strFilt="";
        strSearch="";
        EditText et_search = view.findViewById(R.id.et_search);
        ImageButton btn_search = view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(v ->
                onLoadMore(spinnerFilt.getSelectedItem().toString(),
                et_search.getText().toString(),
                -1)
        );
        //list botanical
        RecyclerView recyclerView = view.findViewById(R.id.RvLibrary);
        loadingDialog = new LoadingDialog(getActivity());

        addapterSub = new AddapterSub(recyclerView, getContext(), documentSnapshots,"");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setAdapter(addapterSub);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition+1 == documentSnapshots.size()) {
                    if (!bl_loadmore) {
                        bl_loadmore = true;
                        onLoadMore(filtDatta[spinnerFilt.getSelectedItemPosition()],
                                et_search.getText().toString(),
                                documentSnapshots.size()-1);
                    }
                }
            }
        });
        spinnerFilt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_search.setText("");
                onLoadMore(filtDatta[spinnerFilt.getSelectedItemPosition()],
                        "",
                        -1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    public void onLoadMore(String rank, String search, int offset) {
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        boolean reset=false;
        if(search==null) search="";
        if(rank==null) rank="";
        loadingDialog.startDialog(rank);
        Query qr = ff.collection(Local.BOTANICALS).limit(Local.LIMIT);
        //load by class or search text
        if (search.equals("")) qr = qr.whereEqualTo(Local.RANK, rank);
        else qr = qr.orderBy(Local.NAMEDEFAULT)
                    .startAt(search)
                    .endAt(search + "\uf8ff");
        if(offset > -1)qr = qr.startAfter(documentSnapshots.get(offset));
        if(!strFilt.equals(rank)) {
            strFilt=rank;
            reset=true;
        }
        if(!strSearch.equals(search)) {
            strSearch=search;
            reset=true;
        }
        //load data
        boolean finalReset = reset;
        qr.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null || task.getResult().size() == 0) {
                            loadingDialog.dismissDialog();
                            return;
                        }
                        //addapterSub.notifyItemRemoved(documentSnapshots.size());
                        if(finalReset) documentSnapshots.clear();
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        addapterSub.notifyDataSetChanged();
                    } else {
                        Toasty.error(requireContext(),getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                    }
                    loadingDialog.dismissDialog();
                    bl_loadmore=false;
                });
    }
    private static class AddapterSub extends LibraryAdapter{
        public AddapterSub(RecyclerView recyclerView, Context mContext, List<QueryDocumentSnapshot> itemViewModels, String out) {
            super(recyclerView, mContext, itemViewModels);
        }

        @Override
        protected void event_onclick(Context context, String key, String name, String outPut) {
            Intent intent = new Intent(context, BitanicalDetailActivity.class);
            intent.putExtra(Local.BundleLocal.KEY, key);
            context.startActivity(intent);
        }
    }

}