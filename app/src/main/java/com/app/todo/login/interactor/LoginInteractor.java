package com.app.todo.login.interactor;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.bridgeit.todoapp.R;
import com.app.todo.model.UserModel;
import com.app.todo.login.presenter.LoginPresenterInterface;
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
    FirebaseAuth firebaseAuth;

    public LoginInteractor(Context context, LoginPresenterInterface loginPresenter) {
        this.context = context;
        this.loginPresenter = loginPresenter;
        firebaseAuth =FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void requestForLogin(String email, String password)
    {
        loginPresenter.showProgressDialog("Please Wait...");
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid=task.getResult().getUser().getUid();
                    getUserloginData(uid);

                }else {
                    loginPresenter.loginFailure(context.getString(R.string.invalid_login));
                }
            }
        });
    }

    public void getUserloginData(final String uid) {

        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
             UserModel model= snapshot.getValue(UserModel.class);
                loginPresenter.loginSuccess(model,uid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
