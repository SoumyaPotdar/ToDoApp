package com.example.bridgeit.todoapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;
import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.presenter.LoginPresenter;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.google.firebase.auth.FirebaseAuth;



public class LoginActivity extends BaseActivity implements LoginViewInterface {

    private String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatEditText useremailedittext, userpasswordedittext;
    AppCompatTextView forgotpasswordtextview, createaccounttextview;
    AppCompatButton userloginbutton;
    AppCompatImageView signingoogleimageview, signinfbimageview;
    String useremail, user_password;
    String emailPattern = Constants.email_pattern;
    LoginPresenter presenter;
    SessionManagement session;
    SharedPreferences userPref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void initView() {

        presenter=new LoginPresenter(this, this);

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
        boolean valid=false;
        if (useremailedittext.getText().toString().length() == 0 || userpasswordedittext.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.blank, Toast.LENGTH_SHORT).show();
            valid= false;
        } else if (useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() >=8 ) {
            valid =true ;
        }
        return valid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbutton:
                useremail = useremailedittext.getText().toString();
                user_password = userpasswordedittext.getText().toString();
                if(validate()) {
                    presenter.requestForLogin(useremail, user_password);
                }
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

    @Override
    public void loginSuccess(UserModel model, String uid) {
        userPref=getApplicationContext().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=userPref.edit();
        editor.putString("keyemail",model.getEmail());
        editor.putString("keyname",model.getFullname());
        editor.putString("uid",uid);
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    ProgressDialog progressDialog;

    @Override
    public void showProgressDialog(String message) {
        if(!isFinishing()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if(!isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }
}