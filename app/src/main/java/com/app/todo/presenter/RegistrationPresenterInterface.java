package com.app.todo.presenter;

public interface RegistrationPresenterInterface {
    public  void  requestForRegister(String name,String email,String mobileno,String password);
    public  void  registerSuccess(String message);

    void registerFailure(String message);

    public  void  showProgressDailog(String message );
    public  void  hideProgressDailog();




}
