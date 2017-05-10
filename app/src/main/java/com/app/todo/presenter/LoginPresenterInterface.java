package com.app.todo.presenter;


import com.app.todo.model.UserModel;

public interface LoginPresenterInterface {
    void requestForLogin(String email, String password );
    void loginSuccess(UserModel model, String uid);
    void loginFailure(String message);
    void  showProgressDialog(String message);
    void  hideProgressDialog();

}
