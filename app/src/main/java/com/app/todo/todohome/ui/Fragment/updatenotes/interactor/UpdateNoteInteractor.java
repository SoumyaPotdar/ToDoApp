package com.app.todo.todohome.ui.Fragment.updatenotes.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.updatenotes.presenter.UpdatePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateNoteInteractor implements UpdateNoteInteractorInterface{
    Context context;
    UpdatePresenter presenter;
    private DatabaseReference databaseReference;
    private String uid;
   // private Calendar currentDate;

    public UpdateNoteInteractor(Context context, UpdatePresenter presenter) {
        this.context=context;
        this.presenter =presenter;

        databaseReference= FirebaseDatabase.getInstance().getReference();
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
       // currentDate= Calendar.getInstance();
    }

    @Override
    public void updateNote(NotesModel notesModel) {
        databaseReference.child("userdata")
                .child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).setValue(notesModel);
        presenter.updateNoteSuccess("note updated");
    }
}

