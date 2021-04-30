package com.example.botanicallibrary.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.bl.LoadingDialog;
import com.example.botanicallibrary.en.Local;
import com.google.common.collect.Lists;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddNewAnswerDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private String idQuestion;
    private LoadingDialog loadingDialog;
    private AdapterSub adapterSub;
    private List<QueryDocumentSnapshot> documentSnapshots;
    private String keySelect;
    private String mail;

    public AddNewAnswerDialog(Activity activity,String idQuestion,String mail){
        if(activity==null) return;
        if(mail==null) return;
        this.activity=activity;
        this.idQuestion=idQuestion;
        loadingDialog=new LoadingDialog(activity);
        documentSnapshots=new ArrayList<>();

    }
    @SuppressLint("SetTextI18n")
    public void startDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        ViewGroup viewGroup= (ViewGroup) layoutInflater.inflate(R.layout.layout_new_answer,null);
        onLoadMore("");
        RecyclerView recyclerView = viewGroup.findViewById(R.id.rv_listPlant);
        ImageView iv_close=viewGroup.findViewById(R.id.iv_close);

        adapterSub = new AdapterSub(recyclerView, activity, documentSnapshots,"");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setAdapter(adapterSub);
        recyclerView.setLayoutManager(gridLayoutManager);

        iv_close.setOnClickListener(v->dismissDialog());
        builder.setView(viewGroup);
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }

    public void onLoadMore(String search) {
        if(documentSnapshots.size() % Local.LIMIT > 0) {
            return;
        }
        loadingDialog.startDialog("Loading...");
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        CollectionReference colRef = ff.collection(Local.BOTANICALS);
        Query qr = colRef.limit(Local.LIMIT);
        //load by class or search text
        if (search == null || search.equals("")) qr = qr.whereEqualTo(Local.RANK, Local.SPECIES);
        else
            qr = qr.orderBy(Local.NAMEDEFAULT)
                    .startAt(search)
                    .endAt(search + "\uf8ff");

        //load data
        qr.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null || task.getResult().size() == 0) {
                            loadingDialog.dismissDialog();
                            return;
                        }
                        documentSnapshots.clear();
                        adapterSub.notifyItemRemoved(documentSnapshots.size());
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        adapterSub.notifyDataSetChanged();
                    } else {
                        Toasty.error(activity,activity.getString(R.string.Error), Toast.LENGTH_SHORT, true).show();
                    }
                    loadingDialog.dismissDialog();
                });
    }

    private class AdapterSub extends LibraryAdapter{
        public AdapterSub(RecyclerView recyclerView, Context mContext, List<QueryDocumentSnapshot> itemViewModels, String out) {
            super(recyclerView, mContext, itemViewModels);
        }

        @Override
        protected void event_onclick(Context context, String key, String name, String outPut) {
            keySelect=key;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            SharedPreferences sp1=activity.getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, Context.MODE_PRIVATE);

            DatabaseReference myRef = database.getReference(Local.firebaseLocal.ANSWERS)
                    .child(idQuestion).child(sp1.getString(Local.DeviceLocal.ID, null));
            myRef.child(Local.firebaseLocal.KEY).setValue(key);
            myRef.child(Local.firebaseLocal.NAME).setValue(name);
            dismissDialog();
        }
    }
}
