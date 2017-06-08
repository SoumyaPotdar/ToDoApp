package com.app.todo.todohome.ui.Fragment.updatenotes.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.updatenotes.presenter.UpdatePresenter;
import com.app.todo.todohome.ui.Fragment.updatenotes.presenter.UpdatePresenterInterface;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class UpdateNoteFragment extends Fragment implements UpdateNoteViewInterface,View.OnClickListener {
    AppCompatTextView datetextview;
    AppCompatEditText titleedittext;
    AppCompatEditText descriptionedittext;
    ToDoMainActivity toDoMainActivity;
    DatePickerDialog datePickerDialog;
    String uid;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;
    String title, description;
    int id;
    Calendar mycalender;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UpdatePresenterInterface presenter;
    String currentDate;
    LinearLayout addNoteFragmentLayout;
    int pos;
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
    private boolean archieve;
    private String reminderdate;
    public int setColor=Color.WHITE;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_note, container, false);
        setHasOptionsMenu(true);

        presenter=new UpdatePresenter(getActivity(),this);
        titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
        datetextview= (AppCompatTextView) view.findViewById(R.id.datetextview);
        descriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
        addNoteFragmentLayout = (LinearLayout) view.findViewById(R.id.addnotefragment);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        notesModel = new NotesModel();
        Bundle bundle = getArguments();

        titleedittext.setText(bundle.getString("title"));
        descriptionedittext.setText(bundle.getString("description"));
        id = bundle.getInt("id");
        archieve = bundle.getBoolean("archieve");
        currentDate = bundle.getString("currentDate");
        datetextview.setText(bundle.getString("reminddate"));
        String color=bundle.getString("color");

        if (color.length() == 1 && color.contains("0")){
            addNoteFragmentLayout.setBackgroundColor(Color.WHITE);
        }else {
            addNoteFragmentLayout.setBackgroundColor(Integer.parseInt(color));
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private void updateLabel() {
        String myFormat = "MMMM dd,yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        datetextview.setText(sdf.format(myCalendar.getTime()));
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
                reminderdate = datetextview.getText().toString();
                notesDataBaseHandler = new NotesDataBaseHandler(getActivity());
                notesModel = new NotesModel();
                notesModel.setNoteDate(currentDate);
                notesModel.setTitle(titleedittext.getText().toString());
                notesModel.setDescription(descriptionedittext.getText().toString());
                notesModel.setReminderDate(datetextview.getText().toString());
                notesModel.setId(id);
                notesModel.setColor(String.valueOf(setColor));
               // notesDataBaseHandler.updateNotes(notesModel);
                presenter.updateNote(notesModel);
                break;

            case R.id.colorpicker:
                ColorPickerDialog.newBuilder().setAllowPresets(true)
                        .setColor(Color.BLACK).setDialogId(0)
                        .setShowAlphaSlider(true)
                        .show(getActivity());
                break;

            case R.id.reminder:
               /* datePickerDialog=new  DatePickerDialog(toDoMainActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();*/
                datePickerDialog = new DatePickerDialog(toDoMainActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateNoteSuccess(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateNoteFailure(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    public void setFragmentBackgroundColor(int color) {
        setColor=color;
       addNoteFragmentLayout.setBackgroundColor(color);
    }
}


