package com.example.bridgeit.todoapp.interactor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.presenter.LoginPresenter;
import com.example.bridgeit.todoapp.presenter.LoginPresenterInterface;
import com.example.bridgeit.todoapp.ui.LoginActivity;
import com.example.bridgeit.todoapp.ui.ToDoMainActivity;
import com.example.bridgeit.todoapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginInteractor implements LoginInteractorInterface{

    Context context;
    LoginPresenterInterface loginPresenter;
    DatabaseReference databaseReference;
    UserModel usermodel;
    FirebaseAuth mAuth;
    SharedPreferences userPref;

    public LoginInteractor(Context context, LoginPresenterInterface loginPresenter) {
        this.context = context;
        this.loginPresenter = loginPresenter;
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void requestForLogin(String email, String password)
    {
        loginPresenter.showProgressDialog("Please Wait...");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid=task.getResult().getUser().getUid();
                    getUserloginData(uid);



                }else {

                }
            }
        });
    }

    public  void getUserloginData(String uid) {

        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
             UserModel model= snapshot.getValue(UserModel.class);
                loginPresenter.loginSuccess(model);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }

        });


    }


}
