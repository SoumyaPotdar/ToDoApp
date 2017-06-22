package com.app.todo.todohome.ui.Fragment.trashnotes.ui;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface TrashFragmentViewInterface {
    void deleteNoteSuccess(String message);
    //void deleteNoteFailure(String message);
    void retriveNoteSuccess(String message);
    void retriveNoteFailure(String message);

    void getNotesSuccess(List<NotesModel> notesModelList);
  //  void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

    //void moveToTrashSuccess(String message);
}
