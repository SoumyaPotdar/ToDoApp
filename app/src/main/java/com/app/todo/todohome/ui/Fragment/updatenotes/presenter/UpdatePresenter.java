package com.app.todo.todohome.ui.Fragment.updatenotes.presenter;

import android.app.Activity;
import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.updatenotes.interactor.UpdateNoteInteractor;
import com.app.todo.todohome.ui.Fragment.updatenotes.interactor.UpdateNoteInteractorInterface;
import com.app.todo.todohome.ui.Fragment.updatenotes.ui.UpdateNoteFragment;

public class UpdatePresenter implements UpdatePresenterInterface {
    Context context;
    UpdateNoteFragment updateNoteView;
    UpdateNoteInteractorInterface interactor;


    public UpdatePresenter(Context context, UpdateNoteFragment updateNoteFragment) {
        this.context=context;
        this.updateNoteView=updateNoteFragment;

        interactor=new UpdateNoteInteractor(context,this);
    }

    @Override
    public void updateNote(NotesModel notesModel) {
        interactor.updateNote(notesModel);
    }

    @Override
    public void updateNoteSuccess(String message) {
        updateNoteView.updateNoteSuccess(message);
    }

    @Override
    public void updateNoteFailure(String message) {
        updateNoteView.updateNoteFailure(message);
    }
}
