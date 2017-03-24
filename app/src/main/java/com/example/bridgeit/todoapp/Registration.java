package com.example.bridgeit.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText mobileno;
    EditText regpassword;
    SharedPreferences pref;
    ImageView iv;
    boolean b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText) findViewById(R.id.nameet);
        email = (EditText) findViewById(R.id.emailet);
        mobileno = (EditText) findViewById(R.id.mobilenoet);
        regpassword = (EditText) findViewById(R.id.passwordet);
        iv = (ImageView) findViewById(R.id.registerimgview);


    }


    public void register(View v) {

        SharedPreferences pref = getSharedPreferences("Reg_Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("namek", name.getText().toString());
        editor.putString("emailk", email.getText().toString());
        editor.putString("mobilek", mobileno.getText().toString());
        editor.putString("passwordk", regpassword.getText().toString());
        b = validate();
        if (b) {

            editor.commit();
           /* pref.getString("namek", null);
            pref.getString("emailk", null);
            pref.getString("mobilek", null);
            pref.getString("passwordk", null);*/
            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

            Intent i = new Intent(Registration.this, Login.class);
            startActivity(i);
            finish();
        } else
            Toast.makeText(this, "Register Again...", Toast.LENGTH_SHORT).show();
    }


   private boolean validate() {

       boolean flag = true;
       String mobilePattern = "^(\\+91-|\\+91|0)?[7-9]{1}([0-9]){9}$";
       String emailPattern = "^[a-zA-Z0-9]{3,}@([a-z]){3,}\\.[a-z]+$";
       int passwordlen = regpassword.length();

       if (name.length() == 0 || mobileno.length() == 0 || email.length() ==
               0 || regpassword.length() == 0) {

           Toast.makeText(getApplicationContext(), "please fill the empty fields", Toast.LENGTH_SHORT).show();
           flag = flag && false;

       } else {
           if (name.length() > 25) {

               Toast.makeText(getApplicationContext(), "please enter less the 25 character in user name", Toast.LENGTH_SHORT).show();
               flag = flag && false;

           }
           if (name.length() == 0 || mobileno.length() == 0 || email.length() ==
                   0 || regpassword.length() == 0) {

               Toast.makeText(getApplicationContext(), "please fill the empty fields", Toast.LENGTH_SHORT).show();
               flag = flag && false;

           }
           if (email.getText().toString().matches(emailPattern)) {

               //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
               flag = flag && true;

           }
           if (!email.getText().toString().matches(emailPattern)) {

               Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
               flag = flag && false;
           }
           if (mobileno.getText().toString().matches(mobilePattern)) {

               // Toast.makeText(getApplicationContext(), "phone number is valid", Toast.LENGTH_SHORT).show();
               Boolean b = mobileno.getText().toString().matches(mobilePattern);
               //Log.v("Mobile Number Display",mobileno.getText().toString());
               //Log.v("flag",b.toString());
               flag = flag && true;

           }
           if (!mobileno.getText().toString().matches(mobilePattern)) {
               Boolean b = mobileno.getText().toString().matches(mobilePattern);
               //Log.v("flag",b.toString());
               //Log.v("Mobile Number Display",mobileno.getText().toString());
               Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
               flag = flag && false;
           }
           if (passwordlen < 8) {
               Toast.makeText(getApplicationContext(), "Password length should be 8 charecters", Toast.LENGTH_SHORT).show();
               flag = flag && false;
           }
       }
           return flag;
    }

}