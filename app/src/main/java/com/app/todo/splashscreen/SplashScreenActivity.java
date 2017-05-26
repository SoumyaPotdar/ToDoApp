package com.app.todo.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.todo.login.ui.LoginActivity;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.example.bridgeit.todoapp.R;
import com.app.todo.baseclass.BaseActivity;
import com.app.todo.utils.Constants;
import com.app.todo.utils.SessionManagement;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends BaseActivity {

    SessionManagement session;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth=FirebaseAuth.getInstance();
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
