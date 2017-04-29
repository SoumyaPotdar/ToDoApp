package com.example.bridgeit.todoapp.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateNoteFragment extends Fragment implements View.OnClickListener {
    AppCompatEditText dateedittext;
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatButton savebutton;
    ToDoMainActivity toDoMainActivity;
    Calendar mycalender;


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
        View view= inflater.inflate(R.layout.fragment_todo_note, container,false);

        dateedittext=(AppCompatEditText)view.findViewById(R.id.datetextview);
        titleedittext=(AppCompatEditText)view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext=(AppCompatEditText)view.findViewById(R.id.fragmentdiscriptionedittext);
        savebutton=(AppCompatButton)view.findViewById(R.id.savedatabutton);
        savebutton.setText("update");
        Bundle bundle=getArguments();
        titleedittext.setText(bundle.getString("date"));
        titleedittext.setText(bundle.getString("title"));
        discriptionedittext.setText(bundle.getString("description"));
        savebutton.setOnClickListener(this);
        dateedittext.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.savedatabutton:
                notesModel=new NotesModel(dateedittext.getText().toString(),titleedittext.getText().toString(), discriptionedittext.getText().toString());
                notesDataBaseHandler.updateNotes(notesModel);
                toDoMainActivity.recyclerAdapter.addItem(notesModel,pos);
               toDoMainActivity.updateRecycler();
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
