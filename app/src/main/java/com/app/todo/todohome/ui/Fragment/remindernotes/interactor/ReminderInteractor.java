package com.app.todo.todohome.ui.Fragment.remindernotes.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.remindernotes.presenter.ReminderPresenterInterface;
import com.app.todo.utils.Connectivity;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ReminderInteractor implements ReminderInteractorInterface {
    Context context;
    ReminderPresenterInterface presenter;
    DatabaseReference databaseReference;
    List<NotesModel> allNotes;
    String currentDate;
    Format format;

    public ReminderInteractor(Context context, ReminderPresenterInterface presenter) {
    this.context=context;
    this.presenter=presenter;
    }

    @Override
    public void getReminderNoteList(final String userId) {
        presenter.showDialog("Fetching data");
           databaseReference= FirebaseDatabase.getInstance().getReference();
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
                            presenter.getReminderNotesSuccess(noteList);
                            presenter.hideDialog();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            presenter.getNotesFailure(context.getString(R.string.some_error));
                            presenter.hideDialog();
                        }
                    }
            );

            presenter.getNotesFailure(context.getString(R.string.no_internet));
            presenter.hideDialog();
        }

    }

