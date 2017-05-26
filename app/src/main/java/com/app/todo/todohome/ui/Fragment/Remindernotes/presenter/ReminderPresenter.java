package com.app.todo.todohome.ui.Fragment.Remindernotes.presenter;

import android.app.Activity;
import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.Remindernotes.interactor.ReminderInteractor;
import com.app.todo.todohome.ui.Fragment.Remindernotes.interactor.ReminderInteractorInterface;
import com.app.todo.todohome.ui.Fragment.Remindernotes.ui.ReminderFragment;
import com.app.todo.todohome.ui.Fragment.Remindernotes.ui.ReminderFragmentViewInterace;

import java.util.List;

public class ReminderPresenter implements ReminderPresenterInterface {
    Context context;
    ReminderFragmentViewInterace viewInterace;
    ReminderInteractorInterface interactor;

    public ReminderPresenter(Context context, ReminderFragmentViewInterace viewInterace) {
        this.context=context;
        this.viewInterace=viewInterace;

        interactor=new ReminderInteractor(context,this);
    }

    @Override
    public void getReminderNoteList(String userId) {
        interactor.getReminderNoteList(userId);
    }

    @Override
    public void getReminderNotesSuccess(List<NotesModel> notesModelList) {
        viewInterace.getReminderNotesSuccess(notesModelList);
    }

    @Override
    public void getNotesFailure(String message) {
        viewInterace.getNotesFailure(message);
    }

    @Override
    public void showDialog(String message) {
        viewInterace.hideDialog();
    }

    @Override
    public void hideDialog() {
        viewInterace.hideDialog();
    }
}
