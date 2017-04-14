package com.example.bridgeit.todoapp.ui;
import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.sqliteDataBase.NotesDataBaseHandler;

public class AddNoteFragment extends Fragment implements View.OnClickListener{
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    public AddNoteFragment(ToDoMainActivity toDoMainActivity) {
        this.toDoMainActivity=toDoMainActivity;
    }

    public AddNoteFragment() {

    }

    public static AddNoteFragment newInstance() {
        Bundle args = new Bundle();
        AddNoteFragment fragment = new AddNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_container, container,false);

        titleedittext=(AppCompatEditText)view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext=(AppCompatEditText)view.findViewById(R.id.fragmentdiscriptionedittext);
        savebutton=(AppCompatButton)view.findViewById(R.id.savedatabutton);
        savebutton.setOnClickListener(this);
       return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.savedatabutton:
                notesDataBaseHandler=new NotesDataBaseHandler(getActivity());
                notesModel=new NotesModel(titleedittext.getText().toString(), discriptionedittext.getText().toString());
                notesDataBaseHandler.addNote(notesModel);
                toDoMainActivity.setBackData(notesModel);
                getActivity().getFragmentManager().popBackStackImmediate();
                break;
        }
    }
}
