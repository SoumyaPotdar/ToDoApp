package com.app.todo.todohome.ui.Fragment.addnotes.ui;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenter;
import com.example.bridgeit.todoapp.R;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteFragment extends Fragment implements AddNoteViewInterface, View.OnClickListener {
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatEditText datepickeredittext;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    String currentDate;
    boolean isReminder;
    String reminderdate;
    FirebaseAuth firebaseAuth;
    SharedPreferences userPref;
    DatePickerDialog datePickerDialog;
    private String uid;
    NotesModel notemod;
    private String TAG="AddNoteFragment";
    private ProgressDialog progressDialog;
    AddNotePresenter presenter;
    public int setColor;
    LinearLayout addnotefragmentLayout;


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

        View view = inflater.inflate(R.layout.fragment_todo_note, container, false);

        setHasOptionsMenu(true);
        titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
        datepickeredittext= (AppCompatEditText) view.findViewById(R.id.dateEdittext);
        addnotefragmentLayout= (LinearLayout) view.findViewById(R.id.addnotefragment);
        datepickeredittext.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString(Constants.keyUserId, "");
        progressDialog=new ProgressDialog(getActivity());
        presenter=new AddNotePresenter(getActivity(),this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateEdittext:
                new DatePickerDialog(toDoMainActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public void putdata(int index, NotesModel model)
    {

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

        String myFormat = "MMMM dd,yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datepickeredittext.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void addNoteSuccess(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showProgressDailog(String message) {
        if (!getActivity().isFinishing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDailog() {
        progressDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.notes_utils,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addnote:
                presenter.showProgressDailog("Adding Note");
                reminderdate=discriptionedittext.getText().toString();
                isReminder=false;
                notesDataBaseHandler = new NotesDataBaseHandler(getActivity());
                notesModel = new NotesModel();

                SimpleDateFormat format=new SimpleDateFormat("MMMM dd,yyyy");
                String currentDate=format.format(new Date().getTime());
                notesModel.setNoteDate(currentDate);
                notesModel.setTitle(titleedittext.getText().toString());
                notesModel.setDescription(discriptionedittext.getText().toString());
                notesModel.setReminderDate(datepickeredittext.getText().toString());
                notesModel.setArchieve(notesModel.isArchieve());
                notesModel.setColor(String.valueOf(setColor));
                notesDataBaseHandler.addNote(notesModel);
                //  toDoMainActivity.setBackData(notesModel);
                //String value=databaseReference.push().getKey();
                presenter.getIndex(notesModel);
                getFragmentManager().popBackStackImmediate();
                presenter.hideProgressDailog();
                break;

            case R.id.colorpicker:
                ColorPickerDialog.newBuilder().setAllowPresets(true)
                        .setColor(Color.BLACK).setDialogId(0)
                        .setShowAlphaSlider(true)
                        .show(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setBackgroundColor(int color) {
        setColor=color;
        addnotefragmentLayout.setBackgroundColor(color);

    }
}



