package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
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
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
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
        initview();
    }

    @Override
    public void initview() {
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
                /*SharedPreferences pref = getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);*/
                useremail = useremailedittext.getText().toString();
                user_password = userpasswordedittext.getText().toString();
                boolean b;
                session = new SessionManagement(this);
                b = validate();
                if (b) {
                        Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                }
                break;


               /*useremail = pref.getString(Constants.keyemail, Constants.value);
                user_password = pref.getString(Constants.keypassword, Constants.value);

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.is_login,true);
                editor.commit();
                Log.i(TAG, "onClick: " + useremail + "vsfd" + user_password);
                boolean b = false;
                b = validate();
                if (b) {
                    if (useremailedittext.getText().toString().equals(useremail) && userpasswordedittext.getText().toString().equals(user_password)) {
                        Intent intent = new Intent(LoginActivity.this, ToDoMainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.not_registered, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                }
                break;
*/
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


}


