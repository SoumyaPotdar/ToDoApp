package com.app.todo.todohome.ui.Fragment.Remindernotes.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface ReminderPresenterInterface {
    void getReminderNoteList(String userId);

    void getReminderNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();
}
