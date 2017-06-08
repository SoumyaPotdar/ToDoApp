package com.app.todo.todohome.ui.Fragment.archivednotes.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface ArchivePresenterInterface {
    void getAllNotelist(String userId);

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();
    void moveToTrash(NotesModel notesModel);
    void moveToTrashSuccess(String message);

    void retriveNote(NotesModel notesModel);

    void retriveNoteSuccess(String message);
}

