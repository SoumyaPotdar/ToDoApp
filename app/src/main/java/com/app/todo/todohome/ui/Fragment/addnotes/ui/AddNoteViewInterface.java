package com.app.todo.todohome.ui.Fragment.addnotes.ui;


public interface AddNoteViewInterface {
    void addNoteSuccess(String message);
    void addNoteFailure(String message);
    void showProgressDailog(String message);
    void hideProgressDailog();
}
