package com.app.todo.todohome.ui.Fragment.addnotes.presenter;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.addnotes.interactor.AddNotesInteractor;
import com.app.todo.todohome.ui.Fragment.addnotes.interactor.AddNotesInteractorInterface;
import com.app.todo.todohome.ui.Fragment.addnotes.ui.AddNoteViewInterface;

public class AddNotePresenter implements AddNotePresenterInterface {
    Context context;
    AddNotesInteractor addNotesInteractor;
    AddNoteViewInterface viewInterface;
    AddNotePresenter presenter;


    public AddNotePresenter(Context context,AddNoteViewInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;
        addNotesInteractor=new AddNotesInteractor(context,this);
    }
    @Override
    public void getIndex(NotesModel notesModel) {
        addNotesInteractor.getIndex(notesModel);
    }

    @Override
    public void putdata(int index, NotesModel model) {
        addNotesInteractor.putdata(index,model);
    }

    @Override
    public void addNoteSuccess(String message) {
        viewInterface.addNoteSuccess(message);
    }

    @Override
    public void addNoteFailure(String message) {
        viewInterface.addNoteFailure(message);
    }

    @Override
    public void showProgressDailog(String message) {
        viewInterface.showProgressDailog(message);
    }

    @Override
    public void hideProgressDailog() {
        viewInterface.hideProgressDailog();
    }
}
