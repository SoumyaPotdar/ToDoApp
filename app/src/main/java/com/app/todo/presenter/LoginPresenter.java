package com.app.todo.presenter;


import android.content.Context;

import com.app.todo.interactor.LoginInteractor;
import com.app.todo.model.UserModel;
import com.app.todo.ui.LoginViewInterface;

public class LoginPresenter implements LoginPresenterInterface {
    LoginInteractor interactor;
    LoginViewInterface viewInterface;

    public LoginPresenter(Context context, LoginViewInterface viewInterface) {
        this.interactor = new LoginInteractor(context, this);
        this.viewInterface = viewInterface;
    }

    @Override
    public void requestForLogin(String email, String password) {
       interactor.requestForLogin(email,password);
    }

    @Override
    public void loginSuccess(UserModel model, String uid) {
        viewInterface.loginSuccess(model,uid);
    }

    @Override
    public void loginFailure(String message) {
        viewInterface.loginFailure(message);
    }

    @Override
    public void showProgressDialog(String message) {
        viewInterface.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog()
    {
        viewInterface.hideProgressDialog();
    }
}
