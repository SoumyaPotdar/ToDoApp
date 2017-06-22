package com.app.todo.todohome.ui.Fragment.remindernotes.ui;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.remindernotes.presenter.ReminderPresenter;
import com.app.todo.todohome.ui.Fragment.remindernotes.presenter.ReminderPresenterInterface;
import com.app.todo.utils.Constants;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static android.content.ContentValues.TAG;
import static com.example.bridgeit.todoapp.R.id.search;

public class ReminderFragment extends Fragment implements ReminderFragmentViewInterace,SearchView.OnQueryTextListener {
    List<NotesModel> models,allNotes;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private boolean isView;
    ToDoMainActivity toDoMainActivity;
    ReminderPresenterInterface presenter;
    ProgressDialog progressDialog;
    String userId;
    String currentDate;
    SharedPreferences userPref;
    Format format;
    private List<NotesModel> searchList;
    private String TAG = "ReminderFragment";


    public ReminderFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes){
        this.toDoMainActivity=toDoMainActivity;
        this.allNotes=allNotes;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_remainder, container, false);
        Fabric.with(getActivity(),new Crashlytics());

        setHasOptionsMenu(true);
        recyclerView= (RecyclerView) view.findViewById(R.id.reminder_recyclerview);
        presenter=new ReminderPresenter(getActivity(),this);
        progressDialog=new ProgressDialog(getActivity());
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        format = new SimpleDateFormat("MMMM dd,yyyy");
        currentDate = format.format(new Date().getTime());
        //presenter.showDialog("Loading...");
        models = new ArrayList<>();

        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        isView = userPref.getBoolean("isList", false);

        presenter.getReminderNoteList(userId);
        checkLayout();
        recyclerAdapter=new RecyclerAdapter(getActivity());
        //presenter.hideDialog();
        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
        getActivity().setTitle("Reminder");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ToDoMainActivity)getActivity()).showOrHideFab(false);
    }
    private void checkLayout() {
        if (isView) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void getReminderNotesSuccess(List<NotesModel> notesModelList) {
            //  presenter.getNotesSuccess(notesModelList);
            allNotes=notesModelList;
            models=getReminderNotes();
            recyclerAdapter.setNoteList(models);
            recyclerView.setAdapter(recyclerAdapter);
        }

    private List<NotesModel> getReminderNotes() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (note.getReminderDate().equals(currentDate)&& !note.isArchieve()) {
                notesModels.add(note);
            }
           // recyclerAdapter=new RecyclerAdapter(getActivity().getBaseContext());

        }
        return notesModels;
    }

    @Override
    public void getNotesFailure(String message) {
       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        //if(!getActivity().isFinishing()&&progressDialog!=null) {
            progressDialog.dismiss();
        //}
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
