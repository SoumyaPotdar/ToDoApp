package com.app.todo.todohome.presenter;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.interactor.TodoMainInteractor;
import com.app.todo.todohome.interactor.TodoMainInteractorInterface;
import com.app.todo.todohome.ui.ToDoMainActivity;
import com.app.todo.todohome.ui.TodoMainActivityInterface;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */

public class TodoMainPresenter implements TodoMainPresenterInterface {

    Context context;
    TodoMainActivityInterface viewInterface;

    TodoMainInteractorInterface interactor;

    public TodoMainPresenter(Context context, TodoMainActivityInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        interactor = new TodoMainInteractor(context, this);
    }

    @Override
    public void getNoteList(String userId) {
        interactor.getNoteList(userId);
    }

    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
        viewInterface.getNotesSuccess(notesModelList);
    }

    @Override
    public void getNotesFailure(String message) {
        viewInterface.getNotesFailure(message);
    }

    @Override
    public void showDialog(String message) {
        viewInterface.showDialog(message);
    }

    @Override
    public void hideDialog() {
        viewInterface.hideDialog();
    }
}
