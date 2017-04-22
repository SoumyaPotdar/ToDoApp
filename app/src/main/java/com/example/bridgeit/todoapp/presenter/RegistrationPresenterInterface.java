package com.example.bridgeit.todoapp.presenter;

public interface RegistrationPresenterInterface {
    public  void  requestForRegister(String name,String email,String mobileno,String password);
    public  void  getRegisterResponse(String response);
    public  void  registerSuccess(String message);
    public  void  registerFailure(String message);
    public  void  showProgressDailog(String message );
    public  void  hideProgressDailog();




}
