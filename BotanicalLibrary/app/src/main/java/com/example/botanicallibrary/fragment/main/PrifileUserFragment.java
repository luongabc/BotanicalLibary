package com.example.botanicallibrary.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.botanicallibrary.R;
import com.example.botanicallibrary.en.Local;
import com.example.botanicallibrary.template.SettingDialog;

public class PrifileUserFragment extends Fragment {
    private boolean isSignin=false;
    private String email;
    private TextView tv_title;


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
            Bundle bundle=getArguments();
            isSignin=bundle.getBoolean(Local.BundleLocal.ISSIGNIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_prifile_user, container, false);
        ImageView iv_setting=view.findViewById(R.id.iv_action);
        tv_title=view.findViewById(R.id.tv_question);
        SharedPreferences sp1=getActivity().getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, getActivity().MODE_PRIVATE);
        email=sp1.getString(Local.DeviceLocal.EMAIL, null);
        if(isSignin)tv_title.setText(email);

        iv_setting.setOnClickListener(v->{
            SettingDialog settingDialog=new SettingDialog(getActivity(),isSignin);
            settingDialog.startDialog();
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle=data.getExtras();
        if(requestCode == Local.REQUEST_CODE_GET_DATA && resultCode == Activity.RESULT_OK) {
            isSignin = (boolean)bundle.get(Local.BundleLocal.ISSIGNIN);
            if(isSignin){
                SharedPreferences sp1=getActivity().getSharedPreferences(Local.DeviceLocal.NAMEDATADEVICE, getActivity().MODE_PRIVATE);
                email=sp1.getString(Local.DeviceLocal.EMAIL, null);
                if(isSignin)tv_title.setText(email);
            }

        }
    }
}