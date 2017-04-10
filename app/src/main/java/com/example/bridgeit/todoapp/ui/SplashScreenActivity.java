package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;

public class SplashScreenActivity extends BaseActivity {

    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
    public void initview() {

    }
}
