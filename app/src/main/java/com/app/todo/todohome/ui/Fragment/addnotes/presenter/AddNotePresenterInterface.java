package com.app.todo.todohome.ui.Fragment.addnotes.presenter;

import com.app.todo.model.NotesModel;

public interface AddNotePresenterInterface {
    void getIndex(final NotesModel notesModel);
    void putdata(int index, NotesModel model);
    void updateNote(NotesModel notesModel);

    void addNoteSuccess(NotesModel notesModel);
    void addNoteFailure(String message);
    void showProgressDailog(String message);
    void hideProgressDailog();
}

