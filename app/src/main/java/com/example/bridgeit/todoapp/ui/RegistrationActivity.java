package com.example.bridgeit.todoapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.presenter.RegistrationPresenter;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class RegistrationActivity extends BaseActivity implements RegistrationViewInterface {
    AppCompatEditText regnameedittext;
    AppCompatEditText regemailedittext;
    AppCompatEditText regmobilenoedittext;
    AppCompatEditText regpasswordedittext;
    DatabaseReference mDatabase;
    String name, email, mobileno, password;
    AppCompatButton savebutton;
    RegistrationPresenter registrationPresenter;
    String userId;
    SharedPreferences userPref;
    ProgressDialog progressDialog;
    SessionManagement session;
    boolean validateresult;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        savebutton.setOnClickListener(this);
    }

    @Override
    public void initView() {
        regnameedittext = (AppCompatEditText) findViewById(R.id.nameedittext);
        regemailedittext = (AppCompatEditText) findViewById(R.id.emailedittext);
        regmobilenoedittext = (AppCompatEditText) findViewById(R.id.mobilenoedittext);
        regpasswordedittext = (AppCompatEditText) findViewById(R.id.passwordedittext);
        savebutton = (AppCompatButton) findViewById(R.id.registrationbutton);
        registrationPresenter = new RegistrationPresenter(this, this);
        progressDialog = new ProgressDialog(this);



    }

    private boolean validate() {
        boolean flag = true;
        String mobilePattern = Constants.mobilepattern;
        String emailPattern = Constants.email_pattern;
        int passwordlen = regpasswordedittext.length();
        if (regnameedittext.length() == 0 || regmobilenoedittext.length() == 0 || regemailedittext.length() ==
                0 || regpasswordedittext.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT).show();
            flag = flag && false;
        } else {
            boolean toast = true;

            if (regnameedittext.length() > 25) {
                if(toast) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                    toast = false;
                }
                flag = flag && false;
            }

            if (regemailedittext.getText().toString().matches(emailPattern)) {
                //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                flag = flag && true;
            }
            if (!regemailedittext.getText().toString().matches(emailPattern)) {
                if(toast){
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    toast=false;
                }
                flag = flag && false;
            }
            if (regmobilenoedittext.getText().toString().matches(mobilePattern)) {
                flag = flag && true;
            }
            if (!regmobilenoedittext.getText().toString().matches(mobilePattern)) {
                if(toast) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
                    toast=false;
                }
                    flag = flag && false;
            }
            if (passwordlen < 8) {
                if(toast) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_pass, Toast.LENGTH_SHORT).show();
                    toast=false;
                }
                flag = flag && false;
            }
        }
        return flag;
    }

    private void updateUser() {
        if (!TextUtils.isEmpty(userId)) {
            mDatabase.child("users").child(userId).child("name").setValue(name);
            mDatabase.child("users").child(userId).child("email").setValue(email);
            mDatabase.child("users").child(userId).child("mobileno").setValue(mobileno);
            mDatabase.child("users").child(userId).child("password").setValue(password);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrationbutton:
                if (validate()) {
                    name = regnameedittext.getText().toString();
                    mobileno = regmobilenoedittext.getText().toString();
                    email = regemailedittext.getText().toString();
                    password = regpasswordedittext.getText().toString();
                    registrationPresenter.requestForRegister(name, email, password, mobileno);
                } else {

                }
                break;

        }
    }


    @Override
    public void registrationSuccess(String message) {
        /*userPref = getApplicationContext().getSharedPreferences(Constants.key_pref, MODE_PRIVATE);
        userPref = getApplicationContext().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("keyname", model.getFullname());
        editor.putString("keyemail", model.getEmail());
        editor.putString("keymobileno", model.getMobileNo());
        editor.putString("keypassword", model.getPassword());
        editor.commit();
*/
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void registrationFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        if(!isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }
}
