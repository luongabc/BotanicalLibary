package com.example.botanicallibrary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.SignUpActivity;

public class PrifileUserFragment extends Fragment {


    public PrifileUserFragment() {
        // Required empty public constructor
    }

    public static PrifileUserFragment newInstance() {
        PrifileUserFragment fragment = new PrifileUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_prifile_user, container, false);
        ImageView avatars=view.findViewById(R.id.avatar);

        avatars.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), SignUpActivity.class);
            startActivity(intent);
        });

        return view;

    }
}