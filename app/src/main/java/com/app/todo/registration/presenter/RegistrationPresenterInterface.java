package com.app.todo.registration.presenter;

import com.app.todo.model.UserModel;

public interface RegistrationPresenterInterface {
    void  requestForRegister(String name,String email,String mobileno,String password);
    void  registerSuccess(UserModel userModel);
    void registerFailure(String message);

    void  showProgressDailog(String message );
    void  hideProgressDailog();




}
