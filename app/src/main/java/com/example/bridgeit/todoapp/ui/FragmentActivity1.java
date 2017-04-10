package com.example.bridgeit.todoapp.ui;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bridgeit.todoapp.R;

public class FragmentActivity1 extends Fragment implements View.OnClickListener{
    AppCompatEditText resdisplayedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    public FragmentActivity1(ToDoMainActivity toDoMainActivity) {
        this.toDoMainActivity=toDoMainActivity;
    }

    public FragmentActivity1() {

    }

    public static FragmentActivity1 newInstance() {

        Bundle args = new Bundle();

        FragmentActivity1 fragment = new FragmentActivity1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_container, container,false);
        resdisplayedittext=(AppCompatEditText)view.findViewById(R.id.fragmentdataedittext);
        savebutton=(AppCompatButton)view.findViewById(R.id.savedatabutton);
        savebutton.setOnClickListener(this);


      /*  String strtext = getArguments().getString("texts");
        resdisplayedittext.setText(strtext);*/
       return  view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.savedatabutton:

                getActivity().getFragmentManager().popBackStackImmediate();
                toDoMainActivity.setBackData(resdisplayedittext.getText().toString());

                break;
        }
    }
}
