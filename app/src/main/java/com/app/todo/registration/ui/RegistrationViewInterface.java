package com.app.todo.registration.ui;

import android.view.View;

import com.app.todo.model.UserModel;

public interface RegistrationViewInterface extends View.OnClickListener {
      void  registrationSuccess(UserModel userModel);
      void registrationFailure(String message);
      void showProgressDailog(String message);
      void hideProgressDailog();
}
