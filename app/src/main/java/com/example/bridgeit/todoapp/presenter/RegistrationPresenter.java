package com.example.bridgeit.todoapp.presenter;

import android.content.Context;

import com.example.bridgeit.todoapp.ui.RegistrationActivity;

public class RegistrationPresenter implements RegistrationPresenterInterface {
    Context context;
    RegistrationPresenter registrationPresenter;

    public RegistrationPresenter(Context context, RegistrationPresenter registrationPresenter) {
        this.context=context;
        this.registrationPresenter=registrationPresenter;


    }

    @Override
    public void requestForRegister(String name, String email, String mobileno, String password) {

    }

    @Override
    public void getRegisterResponse(String response) {

    }

    @Override
    public void registerSuccess(String message) {

    }

    @Override
    public void registerFailure(String message) {

    }

    @Override
    public void showProgressDailog(String message) {

    }

    @Override
    public void hideProgressDailog() {

    }
}
