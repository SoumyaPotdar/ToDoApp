package com.app.todo.todohome.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.presenter.TodoMainPresenter;
import com.app.todo.todohome.presenter.TodoMainPresenterInterface;
import com.app.todo.utils.Connectivity;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoMainInteractor implements TodoMainInteractorInterface {

    Context context;
    TodoMainPresenterInterface presenter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public TodoMainInteractor(Context context, TodoMainPresenterInterface presenter) {
        this.context = context;
        this.presenter = presenter;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void getNoteList(final String userId) {
        presenter.showDialog(context.getString(R.string.loading));

        if (Connectivity.isNetworkConnected(context)){
            databaseReference.child(Constants.key_firebase_userData).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<NotesModel> noteList = new ArrayList<>();
                            GenericTypeIndicator<ArrayList<NotesModel>> t =
                                    new GenericTypeIndicator<ArrayList<NotesModel>>() {
                                    };
                            if(dataSnapshot.child(userId).exists()) {
                                for (DataSnapshot obj : dataSnapshot.child(userId).getChildren()) {
                                    List<NotesModel> li;
                                    li = obj.getValue(t);
                                    noteList.addAll(li);
                                }
                            }
                            noteList.removeAll(Collections.singleton(null));
                            presenter.getNotesSuccess(noteList);
                            presenter.hideDialog();
                         }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                           // presenter.getNotesFailure(context.getString(R.string.some_error));
                            presenter.hideDialog();
                        }
                    }
            );
        }else {
            presenter.getNotesFailure(context.getString(R.string.no_internet));
            presenter.hideDialog();
        }
    }
}
