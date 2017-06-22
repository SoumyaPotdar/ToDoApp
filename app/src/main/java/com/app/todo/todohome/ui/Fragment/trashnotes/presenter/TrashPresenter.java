package com.app.todo.todohome.ui.Fragment.trashnotes.presenter;

import android.content.Context;
import android.widget.Toast;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.trashnotes.interactor.TrashInteractor;
import com.app.todo.todohome.ui.Fragment.trashnotes.interactor.TrashInteractorInterface;
import com.app.todo.todohome.ui.Fragment.trashnotes.ui.TrashFragment;
import com.app.todo.todohome.ui.Fragment.trashnotes.ui.TrashFragmentViewInterface;

import java.util.ArrayList;
import java.util.List;

public class TrashPresenter implements TrashPresenterInterface{
    Context context;
    TrashFragment trashFragmentView;
    TrashInteractorInterface interactor;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<NotesModel> trashNoteList;

    public TrashPresenter(Context context, TrashFragment trashFragmentView) {
        this.context=context;
        this.trashFragmentView=trashFragmentView;

        interactor=new TrashInteractor(context,this);
    }

    @Override
    public void getAllNotelist(String userId) {
        interactor.getAllNotelist(userId);
    }

    @Override
    public void deleteNote(int position, NotesModel notesModel) {
        interactor.deleteNote(position,notesModel);
    }

    @Override
    public void retriveNote(NotesModel notesModel) {
        interactor.retriveNote(notesModel);
    }


    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
        trashFragmentView.getNotesSuccess(notesModelList);
    }
/*
    @Override
    public void getNotesFailure(String message) {
        trashFragmentView.deleteNoteFailure(message);
    }*/

    @Override
    public void showDialog(String message) {
        trashFragmentView.showDialog(message);
    }

    @Override
    public void hideDialog() {
        trashFragmentView.hideDialog();
    }


    @Override
    public void deleteNoteSuccess(String message) {

        trashFragmentView.deleteNoteSuccess(message);
    }

   /* @Override
    public void deleteNoteFailure(String message) {

        trashFragmentView.deleteNoteSuccess(message);
    }
*/
    @Override
    public void retriveNoteSuccess(String message) {
        trashFragmentView.retriveNoteSuccess(message);
    }
/*
    @Override
    public void retriveNoteFailure(String message) {

        trashFragmentView.deleteNoteFailure(message);
    }*/
}
