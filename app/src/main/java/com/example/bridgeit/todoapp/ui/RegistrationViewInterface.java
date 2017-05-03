package com.example.bridgeit.todoapp.ui;

import android.view.View;

import com.example.bridgeit.todoapp.model.UserModel;

public interface RegistrationViewInterface extends View.OnClickListener {
      void  registrationSuccess(String message);
      void registrationFailure(String message);
      void showProgressDailog(String message);
      void hideProgressDailog();
}
