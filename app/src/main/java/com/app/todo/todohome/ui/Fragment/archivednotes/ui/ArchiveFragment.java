package com.app.todo.todohome.ui.Fragment.archivednotes.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.presenter.TodoMainPresenter;
import com.app.todo.todohome.presenter.TodoMainPresenterInterface;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.archivednotes.interactor.ArchiveFragmentInteractor;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenter;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenterInterface;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment implements ArchieveFragmentInterface{
    List<NotesModel> models, allNotes;
    RecyclerView recyclerView;
    ToDoMainActivity toDoMainActivity;
    private boolean isView;
    private RecyclerAdapter recyclerAdapter;
    ArchivePresenterInterface presenter;
    private String userId;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    public ArchiveFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes){
        this.toDoMainActivity=toDoMainActivity;
        this.allNotes = allNotes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_archieve, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.archive_recyclerview);
        progressDialog=new ProgressDialog(getActivity());
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter=new ArchivePresenter(getActivity(),this);

        // fab.setEnabled(false);
        models = new ArrayList<>();
       // presenter.showDialog("Loading...");
         presenter.getArchiveNoteList(userId);
        checkLayout();
        recyclerAdapter=new RecyclerAdapter(getActivity().getBaseContext());
        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
       // presenter.hideDialog();
        getActivity().setTitle("Archive");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ToDoMainActivity)getActivity()).showOrHideFab(false);
    }

   private void checkLayout() {

        if (isView) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        }
    }

    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
        presenter.getNotesSuccess(notesModelList);

    }

    @Override
    public void getNotesFailure(String message) {
        presenter.getNotesFailure(message);
    }

    @Override
    public void showDialog(String message) {
        if(!getActivity().isFinishing()){
          progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (!getActivity().isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }
}

