package com.example.bridgeit.todoapp.ui;

import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Toast;
import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.presenter.LoginPresenter;
import com.example.bridgeit.todoapp.utils.Constants;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;



public class LoginActivity extends BaseActivity implements LoginViewInterface {

    private String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatEditText useremailedittext, userpasswordedittext;
    AppCompatTextView forgotpasswordtextview, createaccounttextview;
    AppCompatButton userloginbutton;
    String useremail, user_password;
    LoginButton fbloginbutton;
    String emailPattern = Constants.email_pattern;
    LoginPresenter presenter;
    SessionManagement session;
    SharedPreferences userPref;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(this);
        initView();

        fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebook(loginResult.getAccessToken());
               /* startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
                finish();           */
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void handleFacebook(AccessToken accessToken) {
        AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), ToDoMainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,R.string.auth_failed,Toast.LENGTH_SHORT).show();

                }
            }


        });

    }

    @Override
    public void initView() {

        presenter=new LoginPresenter(this, this);

        mAuth = FirebaseAuth.getInstance();
        fbloginbutton=(LoginButton)findViewById(R.id.buttonfacebooklogin);
        fbloginbutton.setReadPermissions("email", "public_profile");
        useremailedittext = (AppCompatEditText) findViewById(R.id.loginEdittext);
        userpasswordedittext = (AppCompatEditText) findViewById(R.id.passwordEdittext);
        forgotpasswordtextview = (AppCompatTextView) findViewById(R.id.forgotpassTextview);
        createaccounttextview = (AppCompatTextView) findViewById(R.id.createaccounttextview);
        userloginbutton = (AppCompatButton) findViewById(R.id.loginbutton);

        userloginbutton.setOnClickListener(this);
        forgotpasswordtextview.setOnClickListener(this);
        createaccounttextview.setOnClickListener(this);
    }

    private boolean validate() {
        boolean valid=false;
        if (useremailedittext.getText().toString().length() == 0 || userpasswordedittext.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.blank, Toast.LENGTH_SHORT).show();
            valid= false;
        } else {
            if (useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() >= 6) {
                valid = true;
            }
            if (!useremailedittext.getText().toString().matches(emailPattern) && userpasswordedittext.getText().toString().length() < 6) {
                valid = false;
            }
        }
         return  valid;
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
            case R.id.buttonfacebooklogin:

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}