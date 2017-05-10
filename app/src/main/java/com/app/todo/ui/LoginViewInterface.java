package com.app.todo.ui;

import android.view.View;

import com.app.todo.model.UserModel;

public interface LoginViewInterface extends View.OnClickListener {
       public void loginSuccess(UserModel model, String uid);
       public void loginFailure(String message);
       public void showProgressDialog(String message);
       public void hideProgressDialog();
}
