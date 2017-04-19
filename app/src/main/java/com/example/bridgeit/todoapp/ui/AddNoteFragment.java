package com.example.bridgeit.todoapp.ui;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.sqliteDataBase.NotesDataBaseHandler;
import com.example.bridgeit.todoapp.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNoteFragment extends Fragment implements View.OnClickListener {
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    SharedPreferences userPref;
    private String uid;

    NotesModel notemod;
    public AddNoteFragment(ToDoMainActivity toDoMainActivity) {
        this.toDoMainActivity = toDoMainActivity;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
        savebutton = (AppCompatButton) view.findViewById(R.id.savedatabutton);
        savebutton.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString("uid", "null");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savedatabutton:
                notesDataBaseHandler = new NotesDataBaseHandler(getActivity());
                notesModel = new NotesModel();
                notesModel.setId(0);
                notesModel.setTitle(titleedittext.getText().toString());
                notesModel.setDescription(discriptionedittext.getText().toString());
                notesDataBaseHandler.addNote(notesModel);
                toDoMainActivity.setBackData(notesModel);
                //String value=databaseReference.push().getKey();
                getIndex(notesModel);
                break;
        }
    }

    public void getIndex(final NotesModel notesMode)
    {
        notemod=notesMode;
        final boolean[] flag = {true};

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(notemod!=null) {
                    if(snapshot.child("userdata").child(uid).child("fasdsda").exists()){
                        int index = (int) snapshot.child("userdata").child(uid).child("fasdsda").getChildrenCount();
                        putdata(index, notesMode);
                        notemod = null;
                    } else {
                    putdata(0, notesMode);
                    notemod = null;
                }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }

        });
    }

    public void putdata(int index, NotesModel da)
    {
        da.setId(index);
        getActivity().getFragmentManager().popBackStackImmediate();
        databaseReference.child("userdata").child(uid).child("fasdsda").child(String.valueOf(index)).setValue(da);


    }
}



