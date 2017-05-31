package com.app.todo.todohome.ui.Fragment.notes.presenter;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.notes.interactor.NotesInteractor;
import com.app.todo.todohome.ui.Fragment.notes.interactor.NotesInteractorInterface;
import com.app.todo.todohome.ui.Fragment.notes.ui.NotesFragment;

public class NotesPresenter implements NotesPresenterInterface {
    Context context;
    NotesFragment noteView;
    NotesInteractorInterface interactor;


    public NotesPresenter(Context context, NotesFragment notesFragment) {
        this.context = context;
        this.noteView = notesFragment;

        interactor=new NotesInteractor(context,this);
    }

    @Override
    public void deleteNote(NotesModel notesModel) {
        interactor.deleteNote(notesModel);
    }

    @Override
    public void archiveNote(NotesModel notesModel) {
        interactor.archiveNote(notesModel);
    }

    @Override
    public void undoNote(NotesModel notesModel) {
        interactor.undoNote(notesModel);
    }

    @Override
    public void deleteNoteSuccess(String message) {
        noteView.deleteNoteSuccess(message);
    }

    @Override
    public void deleteNoteFailure(String message) {
        noteView.deleteNoteFailure(message);
    }

    @Override
    public void archiveNoteSuccess(String message) {
        noteView.archiveNoteSuccess(message);
    }

    @Override
    public void archiveNoteFailure(String message) {
        noteView.deleteNoteFailure(message);
    }

    @Override
    public void undoNoteSuccess(String message) {
        noteView.undoNoteSuccess(message);
    }

    @Override
    public void undoNoteFailure(String message) {
        noteView.undoNoteFailure(message);
    }
}
