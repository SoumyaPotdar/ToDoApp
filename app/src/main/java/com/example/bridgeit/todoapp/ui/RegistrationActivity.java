package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatEditText regnameedittext;
    AppCompatEditText regemailedittext;
    AppCompatEditText regmobilenoedittext;
    AppCompatEditText regpasswordedittext;
    DatabaseReference mDatabase;
    String name, email, mobileno, password;
    AppCompatButton savebutton;
    String userId;

    SessionManagement session;
    boolean validateresult;

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
            if (regnameedittext.length() > 25) {

                Toast.makeText(getApplicationContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                flag = flag && false;

                if (regemailedittext.getText().toString().matches(emailPattern)) {
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    flag = flag && true;
                }
                if (!regemailedittext.getText().toString().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    flag = flag && false;
                }
                if (regmobilenoedittext.getText().toString().matches(mobilePattern)) {
/*
                    Boolean b = regmobilenoedittext.getText().toString().matches(mobilePattern);
*/
                    flag = flag && true;
                }
                if (!regmobilenoedittext.getText().toString().matches(mobilePattern)) {
           /*         Boolean b = regmobilenoedittext.getText().toString().matches(mobilePattern);*/
                    Toast.makeText(getApplicationContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
                    flag = flag && false;
                }
                if (passwordlen < 8) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_pass, Toast.LENGTH_SHORT).show();
                    flag = flag && false;
                }
            }
            return flag;
        }
        return  flag;

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
                userRegistration();
              /*  boolean check = validate();
                if (check){
                    userRegistration();
                }
                else{
            Toast.makeText(this, R.string.register_again, Toast.LENGTH_SHORT).show();
        }*/
                break;

        }
    }

    private void userRegistration() {
        name = regnameedittext.getText().toString();
        mobileno = regmobilenoedittext.getText().toString();
        email = regemailedittext.getText().toString();
        password = regpasswordedittext.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter valid Email ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mobileno)){
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter valid Email ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final String userId = task.getResult().getUser().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    UserModel user = new UserModel();
                    user.setFullname(name);
                    user.setEmail(email);
                    user.setMobileNo(mobileno);
                    user.setPassword(password);
                    mDatabase.child("users").child(userId).setValue(user);
                    startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
                    finish();
                    //calltodatabase(userId);
                } /*else {
                    Toast.makeText(RegistrationActivity.this,R.string.register_again, Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private void calltodatabase(String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UserModel user = new UserModel();
        user.setFullname(name);
        user.setEmail(email);
        user.setMobileNo(mobileno);
        user.setPassword(password);
        mDatabase.child("users").child(userId).setValue(user);
        startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
        finish();
    }

}
