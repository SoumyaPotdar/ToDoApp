package com.app.todo.todohome.ui.Fragment.addnotes.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenter;
import com.app.todo.todohome.ui.Fragment.addnotes.presenter.AddNotePresenterInterface;
import com.app.todo.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AddNotesInteractor implements AddNotesInteractorInterface {
    NotesModel notemod;
    Context context;
    SharedPreferences userPref;
    AddNotePresenterInterface presenter;
    private String uid;
    private DatabaseReference databaseReference;

    public AddNotesInteractor(Context context, AddNotePresenterInterface addNotePresenter) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        presenter = addNotePresenter;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void getIndex(final NotesModel notesModel) {
        notemod = notesModel;
        userPref = context.getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        uid = userPref.getString(Constants.keyUserId, "");

        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    GenericTypeIndicator<ArrayList<NotesModel>> t =
                            new GenericTypeIndicator<ArrayList<NotesModel>>() {
                            };
                    List<NotesModel> li = new ArrayList<>();
                    List<NotesModel> noteList = new ArrayList<>();


                    for (DataSnapshot obj : snapshot.child("userdata").child(uid).getChildren()) {
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }

                    if (snapshot.child("userdata").child(uid).child(notesModel.getNoteDate()).exists()) {
                        li = snapshot.child("userdata").child(uid).child(notesModel.getNoteDate()).getValue(t);
                    }

                    if (notemod != null) {
                        int index = li.size();
                        notemod.setSrNo(noteList.size());
                        putdata(index, notemod);
                        notemod = null;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.i(TAG, "getIndex: " + e);
        }
    }

    @Override
    public void putdata(int index, NotesModel notesModel) {
        notesModel.setId(index);
        databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate()).child(String.valueOf(index)).setValue(notesModel);
        presenter.addNoteSuccess(notesModel);
    }

    @Override
    public void updateNote(NotesModel notesModel) {
        databaseReference.child("userdata")
                .child(uid).child(notesModel.getNoteDate())
                .child(String.valueOf(notesModel.getId())).setValue(notesModel);
    }
}
