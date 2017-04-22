package com.example.bridgeit.todoapp.ui;

import android.view.View;

import com.example.bridgeit.todoapp.model.UserModel;

public interface RegistrationViewInterface extends View.OnClickListener {
    public void  registrationSuccess(String message);
    public  void registrationFailure(String message);
    public  void showProgressDailog(String message);
    public  void hideProgressDailog();
}
