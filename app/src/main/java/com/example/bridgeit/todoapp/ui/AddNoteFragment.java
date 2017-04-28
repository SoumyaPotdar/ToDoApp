package com.example.bridgeit.todoapp.ui;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.sqlitedatabase.NotesDataBaseHandler;
import com.example.bridgeit.todoapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener{
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatEditText datepickeredittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    String datedisplay;

    FirebaseAuth firebaseAuth;
    SharedPreferences userPref;
    DatePickerDialog datePickerDialog;
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
        datepickeredittext= (AppCompatEditText) view.findViewById(R.id.dateedittext);
        savebutton = (AppCompatButton) view.findViewById(R.id.savedatabutton);

        savebutton.setOnClickListener(this);
        datepickeredittext.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString("uid", "null");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savedatabutton:

                datedisplay =datepickeredittext.getText().toString();
                notesDataBaseHandler = new NotesDataBaseHandler(getActivity());
                notesModel = new NotesModel();
                notesModel.setDate(datepickeredittext.getText().toString());
                notesModel.setTitle(titleedittext.getText().toString());
                notesModel.setDescription(discriptionedittext.getText().toString());
                notesDataBaseHandler.addNote(notesModel);
                toDoMainActivity.setBackData(notesModel);
                //String value=databaseReference.push().getKey();
                getIndex(notesModel);
                break;

            case R.id.dateedittext:
                new DatePickerDialog(toDoMainActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                    if(snapshot.child("userdata").child(uid).child(datedisplay).exists()){
                        int index = (int) snapshot.child("userdata").child(uid).child(datedisplay).getChildrenCount();
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
        databaseReference.child("userdata").child(uid).child(datedisplay).child(String.valueOf(index)).setValue(da);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datepickeredittext.setText(sdf.format(myCalendar.getTime()));
    }
}



