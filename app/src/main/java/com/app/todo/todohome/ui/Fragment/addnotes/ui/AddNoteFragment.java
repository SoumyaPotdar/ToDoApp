package com.app.todo.todohome.ui.Fragment.addnotes.ui;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.todo.model.NotesModel;
import com.app.todo.reminder.ScheduleClient;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.ui.Activity.DrawerLocker;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenter;
import com.app.todo.utils.Constants;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.fabric.sdk.android.Fabric;

public class AddNoteFragment extends Fragment implements AddNoteViewInterface, View.OnClickListener {
    private static String TAG="AddNoteFragment";
    public int setColor;
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatTextView datepickertextview;
    AppCompatTextView timepickertextview;
    Context context;
    ToDoMainActivity toDoMainActivity;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    private TimePickerDialog timePickerDialog;
    String currentDate;
    boolean isReminder;
    String reminderdate;
    FirebaseAuth firebaseAuth;
    DatePickerDialog datePickerDialog;
    SharedPreferences userPref;
    NotesModel notemod;
    int day,month, years,hour,minute,second;

    private String uid;
    private ProgressDialog progressDialog;
    private ScheduleClient scheduleClient;
    AddNotePresenter presenter;
    LinearLayout addnotefragmentLayout;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            years=year;
            month=monthOfYear;
            day=dayOfMonth;
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // Create a new service client and bind our activity to this service
            scheduleClient = new ScheduleClient(getActivity());
            scheduleClient.doBindService();
            setDateForReminder();
            setTimeforReminder();
        }

    };

    private void setTimeforReminder() {
        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(toDoMainActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour=selectedHour;
                minute=selectedMinute;
                timepickertextview.setText( selectedHour + ":" + selectedMinute);

                        /*lastComma.setVisibility(View.VISIBLE);*/
            }
        }, hour, minute, true);//24hr time
// scheduleClient.setAlarmForNotification(myCalendar);
        timePickerDialog.show();
    }


    public AddNoteFragment(Context context, ToDoMainActivity toDoMainActivity) {
        this.context = context;
        this.toDoMainActivity = toDoMainActivity;
        scheduleClient=new ScheduleClient(context);
        scheduleClient.doBindService();
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

        Fabric.with(getActivity(),new Crashlytics());
        setHasOptionsMenu(true);
        getActivity().setTitle("Notes Add");
        ((DrawerLocker) getActivity()).setDrawerEnabled(false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        titleedittext = (AppCompatEditText) view.findViewById(R.id.fragmenttitledittext);
        discriptionedittext = (AppCompatEditText) view.findViewById(R.id.fragmentdiscriptionedittext);
        datepickertextview = (AppCompatTextView) view.findViewById(R.id.datetextview);
        timepickertextview=(AppCompatTextView)view.findViewById(R.id.remindertimetextview);
        addnotefragmentLayout = (LinearLayout) view.findViewById(R.id.root_layout);

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

    private void setDateForReminder() {

        String myFormat = "MMMM dd,yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datepickertextview.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void addNoteSuccess(NotesModel notesModel) {

        Calendar c = Calendar.getInstance();
        c.set(years, month, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        // c.set(Calendar.SECOND, second);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        Bundle bundle=new Bundle();
        bundle.putString("id", String.valueOf(notesModel.getId()));
        bundle.putString("title",notesModel.getTitle());
        bundle.putString("description", notesModel.getDescription());
        bundle.putString("currentDate", notesModel.getNoteDate());
        bundle.putString("reminddate", notesModel.getReminderDate());
        bundle.putString("color", notesModel.getColor());
        bundle.putString("remindtime",notesModel.getReminderTime());

        scheduleClient.setAlarmForNotification(c,bundle);
    }

    @Override
    public void addNoteFailure(String message) {
        // Toast.makeText(toDoMainActivity, message, Toast.LENGTH_SHORT).show();
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

                //   reminderdate = discriptionedittext.getText().toString();
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
                notesModel.setReminderTime(timepickertextview.getText().toString());

                if(!notesModel.getTitle().equals("") || !notesModel.getDescription().equals(""))  {
                    notesDataBaseHandler.addNote(notesModel);
                    presenter.getIndex(notesModel);
                }
                hideProgressDailog();
                getFragmentManager().popBackStackImmediate();
                break;

          /*  case R.id.remindertimetextview:

                break;
*/
            case R.id.colorpicker:
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

           /* case  R.id.backpress:
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void setBackgroundColor(int color) {
        setColor = color;
        addnotefragmentLayout.setBackgroundColor(color);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ToDoMainActivity) getActivity()).showOrHideFab(false);
    }

   /* @Override
    public void setDrawerEnabled(boolean enabled) {

            int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            drawer.setDrawerLockMode(lockMode);
            toggle.setDrawerIndicatorEnabled(enabled);
        }*/


}



