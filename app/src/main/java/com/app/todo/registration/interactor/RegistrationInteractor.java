package com.app.todo.registration.interactor;

import android.content.Context;

import com.example.bridgeit.todoapp.R;
import com.app.todo.model.UserModel;
import com.app.todo.registration.presenter.RegistrationPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationInteractor   implements  RegistrationInteractorInterface{
    Context context;
    RegistrationPresenter registrationPresenter;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    UserModel user;

    public RegistrationInteractor(Context context, RegistrationPresenter registrationPresenter) {
        this.context = context;
        this.registrationPresenter = registrationPresenter;
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void requestForRegister(final String name, final String email, final String mobileno, final String password) {
        registrationPresenter.showProgressDailog("Please Wait...");
        final UserModel model = new UserModel();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String userId = task.getResult().getUser().getUid();
                    model.setId(userId);
                    model.setPassword(password);
                    model.setFullname(name);
                    model.setMobileNo(mobileno);
                    model.setEmail(email);
                    mDatabase.child(userId).setValue(model);
                    registrationPresenter.registerSuccess(model);
                    registrationPresenter.hideProgressDailog();
                } else {
                    registrationPresenter.registerFailure(context.getString(R.string.reg_failed));
                    registrationPresenter.hideProgressDailog();
                }
            }
        });
    }
}