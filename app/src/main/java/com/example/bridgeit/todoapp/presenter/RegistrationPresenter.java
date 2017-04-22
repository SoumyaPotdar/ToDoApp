package com.example.bridgeit.todoapp.presenter;

import android.content.Context;

import com.example.bridgeit.todoapp.interactor.RegistrationInteractor;
import com.example.bridgeit.todoapp.ui.RegistrationViewInterface;

public class RegistrationPresenter implements RegistrationPresenterInterface {
    Context context;
    RegistrationViewInterface registerViewInterface;
    RegistrationInteractor registrationInteractor;

    public RegistrationPresenter(Context context, RegistrationViewInterface viewInterface) {
        this.context=context;
        this.registerViewInterface = viewInterface;
        this.registrationInteractor = new RegistrationInteractor(context, this);
    }

    @Override
    public void requestForRegister(String name, String email, String mobileno, String password) {
        registrationInteractor.requestForRegister(name, email, mobileno, password);
    }

    @Override
    public void registerSuccess(String message) {
        registerViewInterface.registrationSuccess(message);
    }

    @Override
    public void registerFailure(String message) {
        registerViewInterface.registrationFailure(message);
    }

    @Override
    public void showProgressDailog(String message) {
     registerViewInterface.showProgressDailog(message);
    }

    @Override
    public void hideProgressDailog() {
        registerViewInterface.hideProgressDailog();

    }
}
