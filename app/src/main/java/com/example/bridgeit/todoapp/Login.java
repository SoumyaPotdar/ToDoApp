package com.example.bridgeit.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private  String TAG="Login";
    EditText et1, et2;
    TextView tv1, tv2;
    Button btn;
    ImageView iv1, iv2;
    String useremail, user_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et1 = (EditText) findViewById(R.id.loginEt);
        et2 = (EditText) findViewById(R.id.pswrdEt);
        tv1 = (TextView) findViewById(R.id.forgotTv);
        tv2 = (TextView) findViewById(R.id.createacctv);
        btn = (Button) findViewById(R.id.loginbutton);
        /*iv1 = (ImageView) findViewById(R.id.gglIv);
        iv2 = (ImageView) findViewById(R.id.fbIv);*/
    }

    public void Login(View v) {
        SharedPreferences pref = getSharedPreferences("Reg_Pref", Context.MODE_PRIVATE);

        useremail = pref.getString("emailk", "no value");
        user_password = pref.getString("passwordk", "no value");
        Log.i("  fdfgd", "Login: " + useremail + "  " + user_password);
        boolean b =false;
        b=validate();
        if (b) {
            if (et1.getText().toString().equals(useremail) && et2.getText().toString().equals(user_password)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
             }
            else {
                Toast.makeText(this, "Enter valid username and password \n Login again", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "balnk user name and password", Toast.LENGTH_SHORT).show();
        }
    }
    public void forgotpass(View v) {

    }

    public void CreateAccount(View v) {
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }

    public void Googlesignin(View v) {

    }

    public void Signinfb(View v) {

    }

    private boolean validate() {

        String emailPattern = "^[a-zA-Z0-9]{3,}@([a-z]){3,}\\.[a-z]+$";

        if (et1.getText().toString().length()==0 || et2.getText().toString().length()== 0) {
           // Toast.makeText(getApplicationContext(), "please fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;
        }
       else {
            Log.i(TAG, "validate: ");
            return true;
        }
    }
}




