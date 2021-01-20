package com.example.botanicallibrary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
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
    private final  static String TAG="LibraryFragment";

    private LibraryAdapter libraryAdapter;
    private Spinner spinnerFilt;

    private List<QueryDocumentSnapshot> documentSnapshots;
    protected LoadingDialog loadingDialog;
    protected final int  SIZEDATA=10;
    private  static  final  String[] filtDatta = new String[]{Local.SPECIES,Local.GENUS,Local.ORDER,Local.PHYLUM,Local.FAMILY,Local.CLASS,""};
    TextView btn_after;
    TextView btn_before;

    enum Load{
        AFTER,
        NULL,
        BEFORE
    }
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
        documentSnapshots= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_library, container, false);
        //spinner
        spinnerFilt = view.findViewById(R.id.Spinner_filt);
        ArrayAdapter<String> filtAdapter=new ArrayAdapter<>(view.getContext(),R.layout.support_simple_spinner_dropdown_item, filtDatta);
        filtAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFilt.setAdapter(filtAdapter);

        EditText et_search=view.findViewById(R.id.et_search);
        ImageButton btn_search=view.findViewById(R.id.btn_search);
        btn_before=view.findViewById(R.id.btn_endBefore);
        btn_after=view.findViewById(R.id.btn_startAfter);

        btn_before.setOnClickListener(v->{
            onLoadMore(spinnerFilt.getSelectedItem().toString(),
                    et_search.getText().toString(),
                    0,
                    Load.BEFORE);
        });
        btn_after.setOnClickListener(v->{
            if(documentSnapshots.size() != SIZEDATA) return;
            onLoadMore(spinnerFilt.getSelectedItem().toString(),
                    et_search.getText().toString(),
                    documentSnapshots.size()-1,
                    Load.AFTER);
        });
        btn_search.setOnClickListener(v->{
            onLoadMore(spinnerFilt.getSelectedItem().toString(),
                    et_search.getText().toString(),
                    -1,
                    null);
        });
        //list botanical
        RecyclerView recyclerView = view.findViewById(R.id.RvLibrary);
        loadingDialog=new LoadingDialog(getActivity());

        libraryAdapter=new LibraryAdapter(recyclerView,getContext(),documentSnapshots);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setAdapter(libraryAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        spinnerFilt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_search.setText("");
                onLoadMore(spinnerFilt.getSelectedItem().toString(),
                        null,
                        -1,
                        null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }
    public void onLoadMore(String rank, String search, int offset, Load load) {
        loadingDialog.startDialog("Loading...");
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        CollectionReference colRef=ff.collection(Local.BOTANICALS);
        Query qr=colRef.limit(SIZEDATA);

        //load more or reload
        if(offset>=0){
            if(load.equals(Load.AFTER))
                qr=qr.startAfter(documentSnapshots.get(offset));
            else {
                qr = qr.endBefore(documentSnapshots.get(0));
            }
        }

        //load by class or search text
        if(search==null||search.equals(""))  qr = qr.whereEqualTo(Local.RANK, rank);
        else
            qr=qr.orderBy(Local.NAMEDEFAULT)
                .startAt(search)
                .endAt(search+"\uf8ff");

        //load data
        qr.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult()==null||task.getResult().size()==0) {
                            loadingDialog.dismissDialog();
                            return;
                        }
                        //System.out.println(TAG+": "+ task.getResult().size());
                        documentSnapshots.clear();
                        libraryAdapter.notifyItemRemoved(documentSnapshots.size());
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        libraryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"Get data fail",Toast.LENGTH_LONG).show();
                    }
                    loadingDialog.dismissDialog();
                });
    }
}