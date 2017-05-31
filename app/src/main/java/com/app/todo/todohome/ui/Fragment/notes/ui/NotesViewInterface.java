package com.app.todo.todohome.ui.Fragment.notes.ui;

public interface NotesViewInterface {
    void deleteNoteSuccess(String message);
    void deleteNoteFailure(String message);

    void archiveNoteSuccess(String message);
    void archiveNoteFailure(String message);

    void undoNoteSuccess(String message);
    void undoNoteFailure(String message);

}
