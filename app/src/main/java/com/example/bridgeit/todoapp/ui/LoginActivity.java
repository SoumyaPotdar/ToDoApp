package com.example.bridgeit.todoapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatEditText useremailedittext, userpasswordedittext;
    AppCompatTextView forgotpasswordtextview, createaccounttextview;
    AppCompatButton userloginbutton;
    AppCompatImageView signingoogleimageview, signinfbimageview;
    String useremail, user_password;
    String emailPattern = Constants.email_pattern;
    SessionManagement session;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void initView() {
        useremailedittext = (AppCompatEditText) findViewById(R.id.loginEdittext);
        userpasswordedittext = (AppCompatEditText) findViewById(R.id.passwordEdittext);
        forgotpasswordtextview = (AppCompatTextView) findViewById(R.id.forgotpassTextview);
        createaccounttextview = (AppCompatTextView) findViewById(R.id.createaccounttextview);
        userloginbutton = (AppCompatButton) findViewById(R.id.loginbutton);
        signingoogleimageview = (AppCompatImageView) findViewById(R.id.googleImageview);
        signinfbimageview = (AppCompatImageView) findViewById(R.id.fbImageview);

        userloginbutton.setOnClickListener(this);
        forgotpasswordtextview.setOnClickListener(this);
        createaccounttextview.setOnClickListener(this);
        signingoogleimageview.setOnClickListener(this);
        signinfbimageview.setOnClickListener(this);

    }

    private boolean validate() {

        if (useremailedittext.getText().toString().length() == 0 || userpasswordedittext.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.blank, Toast.LENGTH_SHORT).show();
            return false;
        } else if (useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() == 8) {
            return true && session.login(useremail, user_password);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbutton:
              userlogin();
            break;

            case R.id.createaccounttextview:
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
            break;
            case R.id.forgotpassTextview:

            break;
            case R.id.fbImageview:


            break;
            case R.id.googleImageview:

            break;
        }

}

    public  void userlogin() {
        useremail = useremailedittext.getText().toString();
        user_password = userpasswordedittext.getText().toString();
        if(TextUtils.isEmpty(useremail)){
            Toast.makeText(this, "Enter valid Email ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(user_password)){
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
        }
        mAuth.signInWithEmailAndPassword(useremail, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SharedPreferences userPref;
                    userPref =getApplicationContext().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
                    SharedPreferences.Editor userEditor = userPref.edit();
                    userEditor.putString("uid",task.getResult().getUser().getUid()).commit();
                    finish();
                    startActivity(new Intent(getApplicationContext(),ToDoMainActivity.class));
                    //finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Enter valid Email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }






