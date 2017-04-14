package com.example.bridgeit.todoapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.adapter.RecyclerAdapter;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.sqliteDataBase.NotesDataBaseHandler;

public class UpdateNoteFragment extends Fragment implements View.OnClickListener {
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    int pos;

    public UpdateNoteFragment(ToDoMainActivity toDoMainActivity, int pos) {
        this.toDoMainActivity=toDoMainActivity;
        this.pos = pos;
        notesDataBaseHandler = new NotesDataBaseHandler(toDoMainActivity);
        Log.v("pos",String.valueOf(pos));
    }

    public UpdateNoteFragment() {

    }

   /* public static UpdateNoteFragment newInstance() {
        Bundle args = new Bundle();
        UpdateNoteFragment fragment = new UpdateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_container, container,false);

        titleedittext=(AppCompatEditText)view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext=(AppCompatEditText)view.findViewById(R.id.fragmentdiscriptionedittext);
        savebutton=(AppCompatButton)view.findViewById(R.id.savedatabutton);
        savebutton.setText("update");
        Bundle bundle=getArguments();
        titleedittext.setText(bundle.getString("title"));
        discriptionedittext.setText(bundle.getString("description"));
        savebutton.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.savedatabutton:
                notesModel=new NotesModel(titleedittext.getText().toString(), discriptionedittext.getText().toString());

                notesDataBaseHandler.updateNotes(notesModel);
                toDoMainActivity.recyclerAdapter.addItem(notesModel,pos);
               toDoMainActivity.updateRecycler();
                getActivity().getFragmentManager().popBackStackImmediate();
                break;
        }
    }
}
