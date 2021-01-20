
package com.example.botanicallibrary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botanicallibrary.BitanicalDetailActivity;
import com.example.botanicallibrary.Interface.ItemClickListener;
import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Local;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListQuestionFragment extends Fragment {
    private static final int SIZEDATALOAD=50;
    private List<QueryDocumentSnapshot> documentSnapshots;
    private QuestionAdapter questionAdapter;

    public ListQuestionFragment() {
        // Required empty public constructor
    }

    public static ListQuestionFragment newInstance() {
        ListQuestionFragment fragment = new ListQuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_question, container, false);
        

        return view;
    }
    private void loadData(int offset,boolean loadNext){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference colRef=firebaseFirestore.collection(Local.QUESTION);
        Query qr=colRef.limit(SIZEDATALOAD);

        if(loadNext==true){
            qr=qr.startAfter(documentSnapshots.get(offset));
        }
        else qr = qr.endBefore(documentSnapshots.get(0));
        qr.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult()==null||task.getResult().size()==0) {
                            return;
                        }
                        //System.out.println(TAG+": "+ task.getResult().size());
                        documentSnapshots.clear();
                        questionAdapter.notifyItemRemoved(documentSnapshots.size());
                        documentSnapshots.addAll(Lists.newArrayList(task.getResult()));
                        questionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"Get data fail",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
        private Context mContext;
        private RecyclerView recyclerView;
        private List<QueryDocumentSnapshot> itemViewModels;

        public QuestionAdapter(RecyclerView recyclerView, Context mContext, List<QueryDocumentSnapshot> itemViewModels) {
            this.mContext = mContext;
            this.itemViewModels = itemViewModels;
            this.recyclerView=recyclerView;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(mContext);
            View v=layoutInflater.inflate(R.layout.layout_question,parent,false);
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
            String nameVi=(String)itemViewModels.get(position).get(Local.NAME);
            String url=(String)itemViewModels.get(position).get(Local.IMAGEBG);
            if(nameVi==null)  nameVi=(String)itemViewModels.get(position).get(Local.NAMEDEFAULT);
            holder.textView.setText(nameVi);
            holder.imageView.getLayoutParams().height=recyclerView.getWidth()/2;
            Picasso.with(mContext).load(url).fit().into(holder.imageView);
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if(itemViewModels.size()>1) {
                        String key = itemViewModels.get(position).getId();
                        if(key==null) return;
                        Intent intent = new Intent(view.getContext(), BitanicalDetailActivity.class);
                        intent.putExtra("key", key);
                        view.getContext().startActivity(intent);
                    }
                }
            });
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