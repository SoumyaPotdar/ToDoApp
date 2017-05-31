package com.app.todo.todohome.ui.Fragment.archivednotes.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenter;
import com.app.todo.todohome.ui.Fragment.archivednotes.presenter.ArchivePresenterInterface;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ArchiveFragment extends Fragment implements ArchieveFragmentInterface,SearchView.OnQueryTextListener{
    List<NotesModel> models, allNotes;
    RecyclerView recyclerView;
    ToDoMainActivity toDoMainActivity;
    private boolean isView;
    private RecyclerAdapter recyclerAdapter;
    ArchivePresenterInterface presenter;
    private String userId;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private List<NotesModel> searchList;

    public ArchiveFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes){
        this.toDoMainActivity=toDoMainActivity;
        this.allNotes = allNotes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_archieve, container, false);
        setHasOptionsMenu(true);
        recyclerView= (RecyclerView) view.findViewById(R.id.archive_recyclerview);
        progressDialog=new ProgressDialog(getActivity());
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter=new ArchivePresenter(getActivity(),this);

        // fab.setEnabled(false);
        models = new ArrayList<>();
       // presenter.showDialog("Loading...");
         presenter.getArchiveNoteList(userId);
        checkLayout();
        recyclerAdapter=new RecyclerAdapter(getActivity());
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
      //  presenter.getNotesSuccess(notesModelList);
        allNotes=notesModelList;
        models=getArchiveNotes();

        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
    }


    private List<NotesModel> getArchiveNotes() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if(note.isArchieve()) {
                notesModels.add(note);
            }
        }
        return notesModels;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String search) {
        searchList = new ArrayList<>();
        search = search.toLowerCase();
        Log.e(TAG, "onQueryTextChange: " + models.size());
        for (NotesModel model : models) {
            String title = model.getTitle().toLowerCase();
            if (title.contains(search)) {
                searchList.add(model);
            }
        }
        recyclerAdapter.setNoteList(searchList);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeview:
                SharedPreferences userPref=getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
                if (!isView) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_straggered);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList", true);
                    edit.commit();
                    isView = true;
                } else {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_list);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList", false);
                    edit.commit();
                    isView = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

