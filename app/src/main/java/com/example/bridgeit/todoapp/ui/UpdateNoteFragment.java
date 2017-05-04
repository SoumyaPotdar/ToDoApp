package com.example.bridgeit.todoapp.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.sqlitedatabase.NotesDataBaseHandler;
import com.example.bridgeit.todoapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateNoteFragment extends Fragment implements View.OnClickListener {
    AppCompatEditText dateedittext;
    AppCompatEditText titleedittext;
    AppCompatEditText descriptionedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    String uid;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;
    String title,description;
    int id;
    Calendar mycalender;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentDate;
    int pos;
    private boolean archieve;
    Calendar myCalendar ;
    DatePickerDialog.OnDateSetListener date;
    public UpdateNoteFragment(ToDoMainActivity toDoMainActivity, int pos) {
        this.toDoMainActivity = toDoMainActivity;
        this.pos = pos;
        notesDataBaseHandler = new NotesDataBaseHandler(toDoMainActivity);
        Log.v("pos", String.valueOf(pos));
    }

    public UpdateNoteFragment() {

    }


    public static UpdateNoteFragment newInstance() {
        Bundle args = new Bundle();
        UpdateNoteFragment fragment = new UpdateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_todo_note, container, false);

            dateedittext = (AppCompatEditText) view.findViewById(R.id.dateedittext1);
            titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
            descriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
            savebutton = (AppCompatButton) view.findViewById(R.id.savedatabutton);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            sharedPreferences=getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
            uid=sharedPreferences.getString("uid","");
            firebaseAuth=FirebaseAuth.getInstance();
            uid=firebaseAuth.getCurrentUser().getUid();


            savebutton.setText("update");
            Bundle bundle = getArguments();

            titleedittext.setText(bundle.getString("title"));
            descriptionedittext.setText(bundle.getString("description"));
            id=bundle.getInt("id");
            archieve=bundle.getBoolean("archieve");
            currentDate=bundle.getString("currentDate");
            dateedittext.setText(bundle.getString("reminddate"));
            savebutton.setOnClickListener(this);
            dateedittext.setOnClickListener(this);


            myCalendar = Calendar.getInstance();
            date= new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };
            return view;
        }


        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.savedatabutton:
                    notesModel = new NotesModel();

                    notesModel.setNoteDate(currentDate);
                    notesModel.setTitle(dateedittext.getText().toString());
                    notesModel.setDescription(descriptionedittext.getText().toString());
                    notesModel.setReminderDate(dateedittext.getText().toString());
                    notesModel.setId(id);
                    notesModel.setArchieve(archieve);
                    notesDataBaseHandler.updateNotes(notesModel);
                    toDoMainActivity.recyclerAdapter.addItem(notesModel, pos);
                    toDoMainActivity.updateRecycler();

                    databaseReference.child("userdata").child(uid).child(currentDate).child(String.valueOf(notesModel.getId())).setValue(notesModel);
                    getActivity().getFragmentManager().popBackStackImmediate();
                    break;

                case R.id.dateedittext1:
                    new DatePickerDialog(toDoMainActivity, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
            }
        }




    private void updateLabel() {

        String myFormat = "MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dateedittext.setText(sdf.format(myCalendar.getTime()));
    }
}


