package com.app.todo.registration.ui;

import android.view.View;

public interface RegistrationViewInterface extends View.OnClickListener {
      void  registrationSuccess(String message);
      void registrationFailure(String message);
      void showProgressDailog(String message);
      void hideProgressDailog();
}
