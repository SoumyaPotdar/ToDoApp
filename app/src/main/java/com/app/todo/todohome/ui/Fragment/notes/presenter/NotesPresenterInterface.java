package com.app.todo.todohome.ui.Fragment.notes.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface NotesPresenterInterface {

    void deleteNote(NotesModel notesModel);
    void archiveNote(NotesModel notesModel);
    void undoNote(NotesModel notesModel);

    void moveToTrashSuccess(String message);
    void deleteNoteSuccess(String message);
    void deleteNoteFailure(String message);

    void archiveNoteSuccess(String message);
    void archiveNoteFailure(String message);

    void undoNoteSuccess(String message);
    void undoNoteFailure(String message);

    void putdata(int index, NotesModel notesModel);

    void moveToTrash(NotesModel notesModel);

    void updateSrNo(List<NotesModel> notesModelList);

    // void updateOrder(NotesModel notesModel);
}
