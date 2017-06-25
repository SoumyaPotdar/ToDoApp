package com.app.todo.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.todo.login.ui.LoginActivity;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;
import com.app.todo.baseclass.BaseActivity;
import com.app.todo.utils.Constants;
import com.app.todo.utils.SessionManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends BaseActivity {

    SessionManagement session;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        Fabric.with(this,new Crashlytics());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getInstance().getReferenceFromUrl("https://todoapp-bece4.firebaseio.com/").child("userdata");
        databaseReference.keepSynced(true);

        session = new SessionManagement(this);

        boolean validate = session.isLogin();
        if (validate) {
            Intent intent = new Intent(SplashScreenActivity.this, ToDoMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            callSplashScreen();
        }
    }


    private void callSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, Constants.splash_Timeout);
    }

    @Override
    public void initView() {

    }
}
