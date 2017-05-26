package com.app.todo.todohome.ui.Fragment.archivednotes.interactor;

import android.content.Context;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenter;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenterInterface;

import java.util.ArrayList;

public class ArchiveFragmentInteractor implements ArchiveInteractorInterface{

    ArrayList<NotesModel> allNotes=new ArrayList<>();
    RecyclerAdapter recyclerAdapter;
    ArchivePresenterInterface presenter;
    Context context;

    public ArchiveFragmentInteractor(Context context, ArchivePresenter presenter) {
        this.context=context;
        this.presenter=presenter;
    }

    @Override
    public void getArchiveNoteList(String userId) {
        presenter.showDialog("Fetching data");
       allNotes= (ArrayList<NotesModel>) recyclerAdapter.getAllDataList();
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (note.isArchieve()) {
                notesModels.add(note);
            }
            presenter.getNotesSuccess(notesModels);
            presenter.hideDialog();
        }

    }
}
