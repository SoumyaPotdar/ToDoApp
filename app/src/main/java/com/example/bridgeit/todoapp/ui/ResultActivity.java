package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.bridgeit.todoapp.R;

public class ResultActivity extends Fragment {
    String str;
    AppCompatTextView resdisplaytextview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
       /* View view=inflater.inflate(R.layout.fragment_container,container,false);
        Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
        resdisplaytextview=(AppCompatTextView)view.findViewById(R.id.fragmentdatatextview);
        String strtext = getArguments().getString("texts");
        resdisplaytextview.setText(strtext);*/
        return inflater.inflate(R.layout.fragment_container,container,false);

    }


}
