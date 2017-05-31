package com.app.todo.todohome.ui.Fragment.notes.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.notes.presenter.NotesPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotesInteractor implements NotesInteractorInterface {
    Context context;
    NotesPresenter presenter;
    private DatabaseReference databaseReference;
    NotesModel notesModel;
    String uid;

    public NotesInteractor(Context context, NotesPresenter presenter){
        this.context=context;
        this.presenter=presenter;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        notesModel=new NotesModel();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void deleteNote(NotesModel notesModel) {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).removeValue();
        presenter.deleteNoteSuccess("Note deleted");

    }

    @Override
    public void archiveNote(NotesModel notesModel) {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).setValue(notesModel);
        presenter.archiveNoteSuccess("Note archived");
    }

    @Override
    public void undoNote(NotesModel notesModel) {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).setValue(notesModel);
        presenter.undoNoteSuccess("Undo done");
    }
}
