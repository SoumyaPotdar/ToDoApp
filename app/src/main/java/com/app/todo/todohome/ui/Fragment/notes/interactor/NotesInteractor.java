package com.app.todo.todohome.ui.Fragment.notes.interactor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.notes.presenter.NotesPresenter;
import com.app.todo.utils.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesInteractor implements NotesInteractorInterface {
    Context context;
    NotesPresenter presenter;
    private DatabaseReference databaseReference;
    NotesModel notesModel;
    String uid;
    List<NotesModel> trashNoteList;
    //private RecyclerAdapter recyclerAdapter;

    public NotesInteractor(Context context, NotesPresenter presenter){
        this.context=context;
        this.presenter=presenter;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        notesModel=new NotesModel();
       // recyclerAdapter=new RecyclerAdapter();
        trashNoteList=new ArrayList<>();
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
                .child(String.valueOf(notesModel.getId())).child("archieve").setValue(false);
        presenter.undoNoteSuccess("Undo done");
    }

    @Override
    public void putdata(int index, NotesModel notesModel) {

        notesModel.setId(index);
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate()).child(String.valueOf(index)).setValue(notesModel);
        presenter.moveToTrashSuccess("note moved to Trash");
    }

    @Override
    public void moveToTrash(NotesModel notesModel) {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).setValue(notesModel);
        presenter.moveToTrashSuccess("Note moved to trash");
    }

    @Override
    public void updateSrNo(List<NotesModel> notesModelList) {
        for (NotesModel notesModel : notesModelList) {
            databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                    .child(String.valueOf(notesModel.getId())).child("srNo")
                    .setValue(notesModelList.indexOf(notesModel));
        }
    }
}

