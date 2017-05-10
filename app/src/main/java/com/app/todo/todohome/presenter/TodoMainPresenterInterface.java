package com.app.todo.todohome.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */

public interface TodoMainPresenterInterface {

    void getNoteList(String userId);

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

}