package com.example.bridgeit.todoapp.presenter;


import android.content.Context;

import com.example.bridgeit.todoapp.interactor.LoginInteractor;
import com.example.bridgeit.todoapp.model.UserModel;
import com.example.bridgeit.todoapp.ui.LoginViewInterface;

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
