package com.app.todo.todohome.ui.Fragment.archivednotes.presenter;

import android.content.Context;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.archivednotes.interactor.ArchiveFragmentInteractor;
import com.app.todo.todohome.ui.Fragment.archivednotes.interactor.ArchiveInteractorInterface;
import com.app.todo.todohome.ui.Fragment.archivednotes.ui.ArchieveFragmentInterface;

import java.util.List;

public class ArchivePresenter implements ArchivePresenterInterface {
    Context context;
    ArchiveInteractorInterface interactor;
    ArchieveFragmentInterface viewInterface;

    public ArchivePresenter(Context context, ArchieveFragmentInterface viewInterface) {
        this.viewInterface=viewInterface;
        this.context=context;

        interactor=new ArchiveFragmentInteractor(context,this);
    }

    @Override
    public void getAllNotelist(String userId) {
        interactor.getAllNotelist(userId);
    }

    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
       viewInterface.getNotesSuccess(notesModelList);
    }
/*
    @Override
    public void getNotesFailure(String message) {
       // viewInterface.getNotesFailure(message);
    }*/

    @Override
    public void showDialog(String message) {
        viewInterface.showDialog(message);
    }

    @Override
    public void hideDialog() {
        viewInterface.hideDialog();
    }

    @Override
    public void moveToTrash(NotesModel notesModel) {
        interactor.moveToTrash(notesModel);
    }

    @Override
    public void moveToTrashSuccess(String message) {
        viewInterface.moveToTrashSuccess(message);
    }

    @Override
    public void retriveNote(NotesModel notesModel) {
        interactor.retriveNote(notesModel);
    }

    @Override
    public void retriveNoteSuccess(String message) {
        viewInterface.retriveNoteSuccess(message);
    }
}
