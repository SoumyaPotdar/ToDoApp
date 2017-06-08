package com.app.todo.todohome.ui.Fragment.trashnotes.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface TrashPresenterInterface {

    void deleteNote(int position, NotesModel notesModel);
    void retriveNote(NotesModel notesModel);
    void deleteNoteSuccess(String message);
    void deleteNoteFailure(String message);
    void retriveNoteSuccess(String message);
    void retriveNoteFailure(String message);
    void getAllNotelist(String userId);

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

}
