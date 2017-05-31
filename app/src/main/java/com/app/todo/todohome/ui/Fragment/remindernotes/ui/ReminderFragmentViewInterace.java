package com.app.todo.todohome.ui.Fragment.remindernotes.ui;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface ReminderFragmentViewInterace {
    void getReminderNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();
}
