package com.app.todo.todohome.ui.Fragment.addnotes.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
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
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenter;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteFragment extends Fragment implements AddNoteViewInterface, View.OnClickListener {
    public int setColor;
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatTextView datepickertextview;
    Context context;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    String currentDate;
    boolean isReminder;
    String reminderdate;
    FirebaseAuth firebaseAuth;
    DatePickerDialog datePickerDialog;
    SharedPreferences userPref;
    NotesModel notemod;

    AddNotePresenter presenter;
    LinearLayout addnotefragmentLayout;
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
    private String uid;
    private String TAG = "AddNoteFragment";
    private ProgressDialog progressDialog;

    public AddNoteFragment(Context context, ToDoMainActivity toDoMainActivity) {
        this.context = context;
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
        initView(view);
        return view;
    }

    public void initView(View view) {
        titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
        datepickertextview = (AppCompatTextView) view.findViewById(R.id.datetextview);
        addnotefragmentLayout = (LinearLayout) view.findViewById(R.id.addnotefragment);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString(Constants.keyUserId, "");
        progressDialog = new ProgressDialog(getActivity());
        presenter = new AddNotePresenter(getActivity(), this);
    }

    @Override
    public void onClick(View v) {

    }

    public void putdata(int index, NotesModel model) {

    }

    private void updateLabel() {

        String myFormat = "MMMM dd,yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datepickertextview.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void addNoteSuccess(String message) {
        Toast.makeText(toDoMainActivity, message, Toast.LENGTH_SHORT).show();
      //  getFragmentManager().popBackStackImmediate();
    }

    @Override
    public void addNoteFailure(String message) {
        Toast.makeText(toDoMainActivity, message, Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.notes_utils, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addnote:
                showProgressDailog("Adding Note");
                reminderdate = discriptionedittext.getText().toString();
                isReminder = false;
                notesDataBaseHandler = new NotesDataBaseHandler(getActivity());
                notesModel = new NotesModel();

                SimpleDateFormat format = new SimpleDateFormat("MMMM dd,yyyy");
                String currentDate = format.format(new Date().getTime());
                notesModel.setNoteDate(currentDate);
                notesModel.setTitle(titleedittext.getText().toString());
                notesModel.setDescription(discriptionedittext.getText().toString());
                notesModel.setReminderDate(datepickertextview.getText().toString());
                notesModel.setArchieve(notesModel.isArchieve());
                notesModel.setColor(String.valueOf(setColor));
                notesDataBaseHandler.addNote(notesModel);
                //  toDoMainActivity.setBackData(notesModel);
                //String value=databaseReference.push().getKey();
                presenter.getIndex(notesModel);
             //
                //   getFragmentManager().popBackStackImmediate();
                hideProgressDailog();
                break;

            case R.id.colorpicker:
               /* ColorPickerDialog.newBuilder().setAllowPresets(true)
                        .setColor(Color.YELLOW).setDialogId(0)
                        .setAllowCustom(false)
                        .setShowColorShades(false)
                        .show(getActivity());
                break;*/
                ColorPickerDialog.newBuilder().setAllowPresets(true)
                        .setColor(Color.WHITE).setDialogId(0)
                        .setShowAlphaSlider(true)
                        .show(getActivity());
                break;


            case R.id.reminder:
                datePickerDialog = new DatePickerDialog(toDoMainActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setBackgroundColor(int color) {
        setColor = color;
        addnotefragmentLayout.setBackgroundColor(color);
    }
}



