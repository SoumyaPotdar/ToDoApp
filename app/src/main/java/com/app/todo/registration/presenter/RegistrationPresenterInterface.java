package com.app.todo.registration.presenter;

public interface RegistrationPresenterInterface {
    void  requestForRegister(String name,String email,String mobileno,String password);
    void  registerSuccess(String message);
    void registerFailure(String message);

    void  showProgressDailog(String message );
    void  hideProgressDailog();




}
