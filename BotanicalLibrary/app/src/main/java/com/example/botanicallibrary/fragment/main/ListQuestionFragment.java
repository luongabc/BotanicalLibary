
package com.example.botanicallibrary.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botanicallibrary.FormCreateQuestionActivity;
import com.example.botanicallibrary.Interface.ItemClickListener;
import com.example.botanicallibrary.QuestionDetailActivity;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.template.SettingDialog;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListQuestionFragment extends Fragment {
    private static final int SIZEDATALOAD=50;
    private List<QueryDocumentSnapshot> documentSnapshots;
    private QuestionAdapter questionAdapter;
    private  Boolean isLogin=false;

    public ListQuestionFragment() {
        // Required empty public constructor
    }

    public static ListQuestionFragment newInstance() {
        return new ListQuestionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_question, container, false);
        ImageView iv_addQuestion=view.findViewById(R.id.iv_addQuestion);
        isLogin=getArguments().getBoolean(Local.BundleLocal.ISSIGNIN);

        iv_addQuestion.setOnClickListener(V->{
            if( isLogin){
                Intent intent1 = new Intent(getContext(), FormCreateQuestionActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean(Local.BundleLocal.ISSIGNIN,isLogin);
                intent1.putExtras(bundle);
                startActivityForResult(intent1, Local.REQUEST_CODE_GET_DATA);
            }
            else{
                SettingDialog settingDialog=new SettingDialog(getActivity(),getArguments().getBoolean(Local.BundleLocal.ISSIGNIN));
                settingDialog.startDialog();
            }
        });

        documentSnapshots=new ArrayList<>();
        loadData(0,false);

        RecyclerView recyclerView = view.findViewById(R.id.re_questions);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        questionAdapter= new QuestionAdapter(getContext(),recyclerView,documentSnapshots);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Local.REQUEST_CODE_GET_DATA && resultCode == Activity.RESULT_OK) {
            assert data != null;
            documentSnapshots.clear();
            loadData(0,false);
        }
    }

    private void loadData(int offset,boolean loadNext){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference colRef=firebaseFirestore.collection(Local.QUESTION);
        Query qr=colRef.limit(SIZEDATALOAD);

        if(loadNext==true){
            qr=qr.startAfter(documentSnapshots.get(offset));
        }
        qr.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult()==null||task.getResult().size()==0) return;
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        questionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"Get data fail",Toast.LENGTH_LONG).show();
                    }
                });
    }


    private  class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
        private  Context mContext;
        private  List<QueryDocumentSnapshot> itemViewModels;
        private RecyclerView recyclerView;
        private StorageReference mStorageRef;

        public QuestionAdapter(Context mContext,RecyclerView recyclerView, List<QueryDocumentSnapshot> itemViewModels) {
            this.mContext = mContext;
            this.itemViewModels = itemViewModels;
            this.recyclerView=recyclerView;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(mContext);
            View v=layoutInflater.inflate(R.layout.layout_question,parent,false);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            return new ViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if(itemViewModels.get(position)==null) {
                holder.tv_name.setText(null);
                holder.tv_lable.setText(null);
                holder.tv_date.setText(null);
                holder.iv_image.setImageDrawable(null);
                return;
            }

            holder.tv_name.setText((String) itemViewModels.get(position).get("User"));
            holder.tv_date.setText((String) itemViewModels.get(position).get("date"));
            holder.tv_lable.setText((String)itemViewModels.get(position).get("lable"));

            StorageReference pathReference = mStorageRef.child((String) Objects.requireNonNull(itemViewModels.get(position).get("image")));
            pathReference.getDownloadUrl()
                    .addOnSuccessListener(taskSnapshot -> {
                        Picasso.with(mContext).load(taskSnapshot.toString()).fit().centerInside().into(holder.iv_image);
                    })
                    .addOnFailureListener(exception -> {
                    });
            holder.setItemClickListener((view, position1, isLongClick) -> {
                if(itemViewModels.size()>0){
                    String key=itemViewModels.get(position).getId();
                    if(key==null) return;
                    Intent intent=new Intent(view.getContext(), QuestionDetailActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putString(Local.BundleLocal.KEY,key);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
//            holder.setItemClickListener(new ItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//                    if(itemViewModels.size()>1) {
//                        String key = itemViewModels.get(position).getId();
//                        if(key==null) return;
//                        Intent intent = new Intent(view.getContext(), BitanicalDetailActivity.class);
//                        intent.putExtra("key", key);
//                        view.getContext().startActivity(intent);
//                    }
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return itemViewModels.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ImageView iv_image;
            public TextView tv_date,tv_name,tv_lable;

            public ItemClickListener getItemClickListener() {
                return itemClickListener;
            }

            public void setItemClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            private ItemClickListener itemClickListener;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_image=itemView.findViewById(R.id.iv_image);
                tv_date=itemView.findViewById(R.id.tv_date);
                tv_name=itemView.findViewById(R.id.tv_name);
                tv_lable=itemView.findViewById(R.id.tv_lable);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,getAdapterPosition(),false);
            }

        }
    }
}