package com.app.todo.todohome.ui.Activity;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.app.todo.model.NotesModel;

import java.util.List;


public interface TodoMainActivityInterface extends View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

}