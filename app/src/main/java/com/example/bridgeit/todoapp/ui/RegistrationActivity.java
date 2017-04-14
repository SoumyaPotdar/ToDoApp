package com.example.bridgeit.todoapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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

public class RegistrationActivity extends BaseActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatEditText regnameedittext;
    AppCompatEditText regemailedittext;
    AppCompatEditText regmobilenoedittext;
    AppCompatEditText regpasswordedittext;
    //SharedPreferences pref;
    AppCompatButton savebutton;

     SessionManagement session;
    boolean validateresult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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


    @Override
        public void initView() {
            regnameedittext= (AppCompatEditText) findViewById(R.id.nameedittext);
            regemailedittext= (AppCompatEditText) findViewById(R.id.emailedittext);
            regmobilenoedittext= (AppCompatEditText) findViewById(R.id.mobilenoedittext);
            regpasswordedittext= (AppCompatEditText) findViewById(R.id.passwordedittext);
            savebutton=(AppCompatButton)findViewById(R.id.registrationbutton);
            savebutton.setOnClickListener(this);

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

           }
         /*  if (regnameedittext.length() == 0 || regemailedittext.length() == 0 || regmobilenoedittext.length() ==
                   0 || regpasswordedittext.length() == 0) {

               Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT).show();
               flag = flag && false;*/


           if (regemailedittext.getText().toString().matches(emailPattern)) {

               //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
               flag = flag && true;

           }
           if (!regemailedittext.getText().toString().matches(emailPattern)) {


               Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
               flag = flag && false;
           }
           if (regmobilenoedittext.getText().toString().matches(mobilePattern)) {

            /*   // Toast.makeText(getApplicationContext(), "phone number is valid", Toast.LENGTH_SHORT).show();
               //Log.v("Mobile Number Display",mobileno.getText().toString());
               //Log.v("flag",b.toString());*/
               Boolean b = regmobilenoedittext.getText().toString().matches(mobilePattern);

               flag = flag && true;

           }
           if (!regmobilenoedittext.getText().toString().matches(mobilePattern)) {
               Boolean b = regmobilenoedittext.getText().toString().matches(mobilePattern);
               //Log.v("flag",b.toString());
               //Log.v("Mobile Number Display",mobileno.getText().toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.registrationbutton:

                validateresult= validate();
                /*SharedPreferences pref = getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.keyname, regnameedittext.getText().toString());
                editor.putString(Constants.keyemail, regemailedittext.getText().toString());
                editor.putString(Constants.keymobileno, regmobilenoedittext.getText().toString());
                editor.putString(Constants.keypassword, regpasswordedittext.getText().toString());
                editor.putBoolean(Constants.is_login, false);*/
                session = new SessionManagement(this);
                UserModel model = new UserModel();
                model.setEmail(regemailedittext.getText().toString());
                model.setFullname(regnameedittext.getText().toString());
                model.setMobile(regmobilenoedittext.getText().toString());
                model.setPassword(regpasswordedittext.getText().toString());
                session.register(model);
                if (validateresult) {
                   // editor.commit();
           /* pref.getString("namek", null);
            pref.getString("emailk", null);
            pref.getString("mobilek", null);
            pref.getString("passwordk", null);*/
                    createAccount(regnameedittext.getText().toString(),regemailedittext.getText().toString(),regmobilenoedittext.getText().toString(),regpasswordedittext.getText().toString());
                    Toast.makeText(getApplicationContext(), R.string.reg_success, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(this, R.string.reg_failure, Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount(String name,String email, String mobileno,String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("createacc", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(RegistrationActivity.this, ""+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }



}