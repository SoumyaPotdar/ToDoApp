package com.app.todo.reminder;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;

import com.app.todo.baseclass.BaseActivity;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;

import io.fabric.sdk.android.Fabric;

public class NoteNotificationActivity extends BaseActivity {
    RelativeLayout relativeLayout;
    AppCompatTextView titleedittext;
    AppCompatTextView descriptionedittext;
    AppCompatTextView remindtextview;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_notification);
        Fabric.with(this,new Crashlytics());

        initView();
        titleedittext.setText(bundle.getString("title"));
        descriptionedittext.setText(bundle.getString("description"));
        remindtextview.setText(bundle.getString("reminddate"));
        relativeLayout.setBackgroundColor(Integer.parseInt(bundle.getString("color")));
    }

    @Override
    public void initView() {
        relativeLayout= (RelativeLayout) findViewById(R.id.notificationlayout);
        titleedittext=(AppCompatTextView)findViewById(R.id.titledittext);
        descriptionedittext=(AppCompatTextView)findViewById(R.id.discriptionedittext);
        remindtextview=(AppCompatTextView)findViewById(R.id.remindertextview);
        bundle=getIntent().getExtras();
    }
}
