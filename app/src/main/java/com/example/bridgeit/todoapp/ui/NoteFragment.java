package com.example.bridgeit.todoapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bridgeit.todoapp.R;

/**
 * Created by bridgeit on 14/4/17.
 */
public class NoteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.actvity_recycler,container,false);
        return view;
    }
}