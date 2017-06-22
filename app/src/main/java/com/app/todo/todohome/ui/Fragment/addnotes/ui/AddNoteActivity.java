
package com.app.todo.todohome.ui.Fragment.addnotes.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.app.todo.baseclass.BaseActivity;
import com.app.todo.model.NotesModel;
import com.app.todo.reminder.ScheduleClient;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenter;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends BaseActivity implements AddNoteViewInterface
        ,View.OnClickListener,ColorPickerDialogListener {
    private static String TAG="AddNoteFragment";
    public int setColor;
    AppCompatEditText titleedittext;
    AppCompatEditText discriptionedittext;
    AppCompatTextView datepickertextview;
    AppCompatTextView timepickertextview;
    AppCompatImageView colorpickerImageView;
    AppCompatImageView reminderImageView;
    AppCompatImageView saveImageView;
    AppCompatImageView backImageView;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    boolean flagForUpdate;
    Bundle bundle;
    DatabaseReference databaseReference;
    private TimePickerDialog timePickerDialog;
    FirebaseAuth firebaseAuth;
    DatePickerDialog datePickerDialog;
    SharedPreferences userPref;
    int day,month, years,hour,minute,second;
    private String uid;
    private ProgressDialog progressDialog;
    private ScheduleClient scheduleClient;
    AddNotePresenter presenter;
    LinearLayout addNoteActivityLayout;

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
            scheduleClient = new ScheduleClient(AddNoteActivity.this);
            scheduleClient.doBindService();
            setDateForReminder();
            setTimeforReminder();
        }

    };

    private void setTimeforReminder() {
        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.fragment_todo_note);
        initView();

        bundle=getIntent().getExtras();
        if(bundle==null) {
            flagForUpdate =false;
            setTitle("Notes Add");
        }else{
            flagForUpdate =true;
            setTitle("Note update");
            titleedittext.setText(bundle.getString("title"));
            discriptionedittext.setText(bundle.getString("description"));
            datepickertextview.setText(bundle.getString("reminddate"));
            timepickertextview.setText(bundle.getString("remindtime"));
            addNoteActivityLayout.setBackgroundColor(Integer.parseInt(bundle.getString("color")));
        }
    }

    public void initView() {
        titleedittext = (AppCompatEditText)findViewById(R.id.fragmenttitledittext);
        discriptionedittext = (AppCompatEditText)findViewById(R.id.fragmentdiscriptionedittext);
        datepickertextview = (AppCompatTextView)findViewById(R.id.datetextview);
        timepickertextview=(AppCompatTextView)findViewById(R.id.remindertimetextview);
        colorpickerImageView=(AppCompatImageView)findViewById(R.id.colorpicker_imageview);
        backImageView=(AppCompatImageView)findViewById(R.id.back_imageview);
        reminderImageView=(AppCompatImageView)findViewById(R.id.reminder_imageview);
        saveImageView=(AppCompatImageView)findViewById(R.id.save_imageview);

        addNoteActivityLayout = (LinearLayout)findViewById(R.id.root_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userPref = getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString(Constants.keyUserId, "");
        progressDialog = new ProgressDialog(this);
        presenter = new AddNotePresenter(this, this);

        scheduleClient=new ScheduleClient(this);
        scheduleClient.doBindService();

        colorpickerImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        reminderImageView.setOnClickListener(this);
        saveImageView.setOnClickListener(this);
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
        scheduleClient.setAlarmForNotification(myCalendar,bundle);
    }

    @Override
    public void addNoteFailure(String message) {

    }

    @Override
    public void showProgressDailog(String message) {
        if (!isFinishing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDailog() {
        progressDialog.dismiss();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_imageview:
                showProgressDailog("Adding Note");
                //   reminderdate = discriptionedittext.getText().toString();
                notesDataBaseHandler = new NotesDataBaseHandler(this);
                notesModel = new NotesModel();

                SimpleDateFormat format = new SimpleDateFormat("MMMM dd,yyyy");
                String currentDate = format.format(new Date().getTime());
                if(flagForUpdate){
                    notesModel.setId(bundle.getInt("id"));
                    notesModel.setNoteDate(bundle.getString("currentDate"));
                }else {
                    notesModel.setNoteDate(currentDate);
                }
                    notesModel.setTitle(titleedittext.getText().toString());
                    notesModel.setDescription(discriptionedittext.getText().toString());
                    notesModel.setReminderDate(datepickertextview.getText().toString());
                    notesModel.setArchieve(notesModel.isArchieve());
                    notesModel.setColor(String.valueOf(setColor));
                    notesModel.setReminderTime(timepickertextview.getText().toString());

                if(!notesModel.getTitle().equals("") || !notesModel.getDescription().equals("")) {
                    if (flagForUpdate) {
                        presenter.updateNote(notesModel);
                    } else {
                        notesDataBaseHandler.addNote(notesModel);
                        presenter.getIndex(notesModel);
                    }
                }
                hideProgressDailog();
                getFragmentManager().popBackStackImmediate();
                finish();
                break;

            case R.id.colorpicker_imageview:
                ColorPickerDialog.newBuilder().setAllowPresets(true)
                        .setColor(Color.WHITE).setDialogId(0)
                        .setShowAlphaSlider(true)
                        .show(this);
                break;

            case R.id.reminder_imageview:
                datePickerDialog = new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.back_imageview:
                finish();
                break;
        }
    }

    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        if (dialogId == 0) {
            setColor=color;
                addNoteActivityLayout.setBackgroundColor(color);
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}