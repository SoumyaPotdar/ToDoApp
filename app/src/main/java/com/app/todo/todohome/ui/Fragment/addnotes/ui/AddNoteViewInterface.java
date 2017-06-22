package com.app.todo.todohome.ui.Fragment.addnotes.ui;


import com.app.todo.model.NotesModel;

public interface AddNoteViewInterface {
    void addNoteSuccess(NotesModel notesModel);
    void addNoteFailure(String message);
    void showProgressDailog(String message);
    void hideProgressDailog();
}
