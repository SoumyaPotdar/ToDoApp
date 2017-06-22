package com.app.todo.todohome.ui.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public  class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.about_fragment,container,false);
        getActivity().setTitle("About");
        Fabric.with(getActivity(),new Crashlytics());

        return view;
    }

    public AboutFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes) {
    }
}
