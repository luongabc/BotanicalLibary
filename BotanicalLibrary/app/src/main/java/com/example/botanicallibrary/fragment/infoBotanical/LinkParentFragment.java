package com.example.botanicallibrary.fragment.infoBotanical;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.botanicallibrary.BitanicalDetailActivity;
import com.example.botanicallibrary.ChangeDetailActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.template.SettingDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class LinkParentFragment extends Fragment implements View.OnClickListener{
    private String key;
    private LinearLayout listPrent;
    private TextView tv_name;
    private String keyUser;
    private final LinearLayout.LayoutParams a=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams lp_tv_nameRank=new LinearLayout.LayoutParams(300,LinearLayout.LayoutParams.WRAP_CONTENT,0);
    private final LinearLayout.LayoutParams lp_tv_name=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
    private final LinearLayout.LayoutParams lp_v=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2,1);
    private int keyCheckLoad;
    private LoadingDialog loadingDialog;


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
        key=getArguments().getString(Local.BundleLocal.KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.startDialog(getString(R.string.loading));
        keyCheckLoad=0;
        // Inflate the layout for this fragment
        a.setMargins(3,3,3,3);
        lp_tv_nameRank.setMargins(20,3,3,3);
        lp_tv_name.setMargins(3,3,20,3);
        View view= inflater.inflate(R.layout.fragment_link_parent, container, false);
        listPrent=view.findViewById(R.id.ll_listParent);
        LinearLayout detail = view.findViewById(R.id.ll_description);
        tv_name=view.findViewById(R.id.tv_name_species);
        setParent(key);
        setDetail(detail,key);
        ImageView iv_detail=view.findViewById(R.id.btn_edit);
        iv_detail.setOnClickListener(v->{
            SharedPreferences sp1=getContext().getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE,getContext().MODE_PRIVATE);
            keyUser=sp1.getString(Local.DeviceLocal.ID,null);
            if(keyUser==null){
                SettingDialog settingDialog=new SettingDialog(getActivity(),false);
                settingDialog.startDialog();
            }
            else {
                Intent intent = new Intent(getContext(), ChangeDetailActivity.class);
                intent.putExtras(getArguments());
                startActivity(intent);
            }
        });

        return view;
    }
    @SuppressLint("InflateParams")
    private void setDetail(LinearLayout linearLayout, String key){
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection(Local.firebaseLocal.BOTANICALS)
                .document(key)
                .collection(Local.firebaseLocal.DESCRIPTION)
                .orderBy(Local.firebaseLocal.NUMBERORDER)
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())) {
                        System.out.println(documentSnapshot.getData());
                        String urlImage=(String) documentSnapshot.get(Local.firebaseLocal.IMAGE);
                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                        @SuppressLint("InflateParams") ViewGroup v;
                        if(urlImage!=null){
                            v= (ViewGroup) layoutInflater.inflate(R.layout.layout_card_description, null);
                            ImageView iv_description = v.findViewById(R.id.iv_description);
                            Picasso.with(getContext()).load(urlImage).into(iv_description);
                        }
                        else v=(ViewGroup) layoutInflater.inflate(R.layout.layout_card_description2, null);
                        EditText editName = v.findViewById(R.id.name);
                        editName.setCursorVisible(false);
                        editName.setLongClickable(false);
                        editName.setClickable(false);
                        editName.setFocusable(false);
                        editName.setSelected(false);
                        editName.setKeyListener(null);
                        editName.setBackgroundResource(android.R.color.transparent);

                        EditText editContent = v.findViewById(R.id.content);
                        editContent.setCursorVisible(false);
                        editContent.setLongClickable(false);
                        editContent.setClickable(false);
                        editContent.setFocusable(false);
                        editContent.setSelected(false);
                        editContent.setKeyListener(null);
                        editContent.setBackgroundResource(android.R.color.transparent);

                        editName.setText((String) documentSnapshot.get(Local.firebaseLocal.NAME));
                        editContent.setText((String) documentSnapshot.get(Local.firebaseLocal.CONTENT));
                        linearLayout.addView(v);

                    }
                    if(keyCheckLoad==1){
                        loadingDialog.dismissDialog();
                    }
                    else keyCheckLoad=1;
                });
    }
    private void setParent(@NonNull String key){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Local.BOTANICALS)
                .document(key)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String name= (String) Objects.requireNonNull(task.getResult()).get(Local.NAME);
                        if(name==null || name.equals("")) name=(String) task.getResult().get(Local.NAMEDEFAULT);
                        if(task.getResult().getId().equals(this.key)) tv_name.setText(name);
                        String rank=getRank((String) Objects.requireNonNull(task.getResult().get(Local.RANK)));

                        LinearLayout ll=new LinearLayout(getContext());
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setLayoutParams(a);

                        TextView nameRank=new TextView(getContext());
                        nameRank.setLayoutParams(lp_tv_nameRank);
                        nameRank.setText(rank);

                        TextView nameDe =new TextView(getContext());
                        nameDe.setLayoutParams(lp_tv_name);
                        nameDe.setText(name);
                        nameDe.setOnClickListener(v -> {
                            Intent intent =new Intent(getContext(),BitanicalDetailActivity.class);
                            intent.putExtra(Local.BundleLocal.KEY,key);
                            startActivity(intent);
                            requireActivity().finish();
                        });

                        ll.addView(nameRank);
                        ll.addView(nameDe);
                        listPrent.addView(ll);
                        View view =new View(getContext());
                        view.setLayoutParams(lp_v);
                        view.setBackgroundResource(R.color.gray_600);
                        listPrent.addView(view);
                        if(task.getResult().get(Local.PARENTKEY)==null){
                            if(keyCheckLoad==1){
                                loadingDialog.dismissDialog();
                            }
                            else keyCheckLoad=1;
                            return;
                        }
                        setParent(Objects.requireNonNull(task.getResult().get(Local.PARENTKEY)).toString());
                    }
                });
    }
    private String getRank(String rank){
        if(rank.equals(Local.SPECIES)) return ("Loài (").concat(Local.SPECIES).concat(")");
        if(rank.equals(Local.GENUS)) return ("Chi (").concat(Local.GENUS).concat(")");
        if(rank.equals(Local.FAMILY)) return ("Họ (").concat(Local.FAMILY).concat(")");
        if(rank.equals(Local.ORDER)) return ("Bộ (").concat(Local.ORDER).concat(")");
        if(rank.equals(Local.PHYLUM)) return ("Ngành (").concat(Local.PHYLUM).concat(")");
        if(rank.equals(Local.CLASS)) return ("Lớp (").concat(Local.CLASS).concat(")");
        if(rank.equals("KING")) return ("Giới (KINGDOM)");
        return null;
    }
    @Override
    public void onClick(View v) {
        String key= (String) ((Button)v).getContentDescription();
        this.onDestroy();
        Intent intent=new Intent(getContext(),BitanicalDetailActivity.class);
        intent.putExtra(Local.BundleLocal.KEY   ,key);
        startActivity(intent);
    }
}