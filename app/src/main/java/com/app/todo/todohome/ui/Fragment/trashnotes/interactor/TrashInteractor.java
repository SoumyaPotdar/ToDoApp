package com.app.todo.todohome.ui.Fragment.trashnotes.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.trashnotes.presenter.TrashPresenter;
import com.app.todo.utils.Connectivity;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrashInteractor implements TrashInteractorInterface{
    Context context;
    TrashPresenter trashPresenter;
    private DatabaseReference databaseReference;
    String uid;

    public TrashInteractor(Context context, TrashPresenter trashPresenter) {
        this.context=context;
        this.trashPresenter=trashPresenter;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void getAllNotelist(final String userId) {
        trashPresenter.showDialog("Fetching data");
            databaseReference.child(Constants.key_firebase_userData).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<NotesModel> noteList = new ArrayList<>();
                            GenericTypeIndicator<ArrayList<NotesModel>> t =
                                    new GenericTypeIndicator<ArrayList<NotesModel>>() {
                                    };

                            for (DataSnapshot obj : dataSnapshot.child(userId).getChildren()) {
                                List<NotesModel> li = new ArrayList<>();
                                li.addAll(obj.getValue(t));
                                noteList.addAll(li);
                            }
                            noteList.removeAll(Collections.singleton(null));
                            trashPresenter.getNotesSuccess(noteList);
                            trashPresenter.hideDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                          //  trashPresenter.getNotesFailure(context.getString(R.string.some_error));
                            trashPresenter.hideDialog();
                        }
                    }
            );

            //trashPresenter.getNotesFailure(context.getString(R.string.no_internet));
            trashPresenter.hideDialog();
        }

    @Override
    public void deleteNote(int position, NotesModel notesModel)
    {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).removeValue();

        trashPresenter.deleteNoteSuccess("Note deleted");

    }

    @Override
    public void retriveNote(NotesModel notesModel) {
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).child("trash").setValue(false);
        trashPresenter.retriveNoteSuccess("Note retrived");
    }
}
