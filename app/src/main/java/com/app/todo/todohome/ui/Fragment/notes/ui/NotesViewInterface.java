package com.app.todo.todohome.ui.Fragment.notes.ui;

import com.app.todo.model.NotesModel;

public interface NotesViewInterface {
    void moveToTrashSuccess(String message);
    void moveToTrashFailure(String message);

    void deleteNoteSuccess(String message);
    void deleteNoteFailure(String message);

    void archiveNoteSuccess(String message);
    void archiveNoteFailure(String message);

    void undoNoteSuccess(String message);
    void undoNoteFailure(String message);
}
