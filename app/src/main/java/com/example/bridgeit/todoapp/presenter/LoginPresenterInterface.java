package com.example.bridgeit.todoapp.presenter;


import com.example.bridgeit.todoapp.model.UserModel;

public interface LoginPresenterInterface {
    void requestForLogin(String email, String password );
    void loginSuccess(UserModel model);
    void loginFailure(String message);
    void  showProgressDialog(String message);
    void  hideProgressDialog();

}
