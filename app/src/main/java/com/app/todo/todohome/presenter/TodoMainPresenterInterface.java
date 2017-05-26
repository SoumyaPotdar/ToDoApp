package com.app.todo.todohome.presenter;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface TodoMainPresenterInterface {

    void getNoteList(String userId);

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

}