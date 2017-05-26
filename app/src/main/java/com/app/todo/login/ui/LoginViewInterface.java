package com.app.todo.login.ui;

import android.view.View;

import com.app.todo.model.UserModel;

public interface LoginViewInterface extends View.OnClickListener {
        void loginSuccess(UserModel model, String uid);
        void loginFailure(String message);
        void showProgressDialog(String message);
        void hideProgressDialog();
}
