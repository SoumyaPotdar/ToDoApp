package com.app.todo.todohome.ui.Fragment.archivednotes.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface ArchivePresenterInterface {
    void getArchiveNoteList(String userId);

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();
}
