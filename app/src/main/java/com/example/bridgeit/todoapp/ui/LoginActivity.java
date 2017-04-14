package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
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
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("signin", "onAuthStateChanged:signed in" + user.getUid());
                } else
                    //user is signed out
                    Log.d("signout", "onAuthStateChanged:signed out");
            }

        };
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("login", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("loginfail", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "login done", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        mAuth = FirebaseAuth.getInstance();

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
                        signin(useremailedittext.getText().toString(),userpasswordedittext.getText().toString());
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


