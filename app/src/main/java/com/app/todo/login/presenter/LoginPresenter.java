package com.app.todo.login.presenter;


import android.content.Context;

import com.app.todo.login.interactor.LoginInteractor;
import com.app.todo.model.UserModel;
import com.app.todo.login.ui.LoginViewInterface;

public class LoginPresenter implements LoginPresenterInterface {
    Context context;
    LoginInteractor interactor;
    LoginViewInterface viewInterface;

    public LoginPresenter(Context context, LoginViewInterface viewInterface) {
        this.context=context;
        this.viewInterface = viewInterface;

        this.interactor = new LoginInteractor(context, this);


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
