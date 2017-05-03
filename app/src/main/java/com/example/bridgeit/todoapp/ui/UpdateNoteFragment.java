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

    int pos;

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

            dateedittext = (AppCompatEditText) view.findViewById(R.id.datetextview);
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
            titleedittext.setText(bundle.getString("date"));
            titleedittext.setText(bundle.getString("title"));
            descriptionedittext.setText(bundle.getString("description"));
            savebutton.setOnClickListener(this);
            dateedittext.setOnClickListener(this);

            if(bundle!=null) {
                title = titleedittext.getText().toString();
                description = descriptionedittext.getText().toString();
            }

           if(bundle.containsKey("id"))
               id=(bundle.getInt("id"));
            titleedittext.setText(title);
            descriptionedittext.setText(description);
            SimpleDateFormat format=new SimpleDateFormat("MMMM dd ,yyyy");
            String currentDate=format.format(new Date().getTime());

            return view;
        }


        @Override
        public void onClick (View v){
            switch (v.getId()) {
               /* database = new NoteDatabase(getActivity());
                todoHomeDataModel = new TodoHomeDataModel();
                todoHomeDataModel.setTitle(fragmentlistview_TitleEditText.getText().toString());
                todoHomeDataModel.setDescription(fragmentlistview_DescriptionEditText.getText().toString());
                todoHomeDataModel.setId(id);
                database.updateItem(todoHomeDataModel);
                mfirebasedatabase.child("note_details").child(uid).child(currentDate).child(String.valueOf(todoHomeDataModel.getId())).setValue(todoHomeDataModel);
                getActivity().getFragmentManager().popBackStackImmediate*/

                case R.id.savedatabutton:
                    notesModel = new NotesModel(dateedittext.getText().toString(), titleedittext.getText().toString(), descriptionedittext.getText().toString());
                    toDoMainActivity.recyclerAdapter.addItem(notesModel, pos);
                    toDoMainActivity.updateRecycler();

                    notesModel.setTitle(dateedittext.getText().toString());
                    notesModel.setDescription(descriptionedittext.getText().toString());
                    notesModel.setId(id);
                    notesDataBaseHandler.updateNotes(notesModel);
                    databaseReference.child("userdata").child(uid).child(String.valueOf(notesModel.getId())).setValue(notesModel);
                    getActivity().getFragmentManager().popBackStackImmediate();
                    break;

                case R.id.dateedittext:
                    new DatePickerDialog(toDoMainActivity, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
            }
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

        dateedittext.setText(sdf.format(myCalendar.getTime()));
    }
}


