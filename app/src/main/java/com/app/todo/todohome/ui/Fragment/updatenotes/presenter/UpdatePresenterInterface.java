package com.app.todo.todohome.ui.Fragment.updatenotes.presenter;

import com.app.todo.model.NotesModel;

public interface UpdatePresenterInterface {
    void updateNote(NotesModel notesModel);
    void updateNoteSuccess(String message);
    void updateNoteFailure(String message);
}
