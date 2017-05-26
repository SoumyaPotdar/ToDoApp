package com.app.todo.todohome.ui.Fragment.Remindernotes.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.todohome.presenter.TodoMainPresenter;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.Remindernotes.presenter.ReminderPresenter;
import com.app.todo.todohome.ui.Fragment.Remindernotes.presenter.ReminderPresenterInterface;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderFragment extends Fragment implements ReminderFragmentViewInterace {
    List<NotesModel> models,allNotes;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private boolean isView;
    ToDoMainActivity toDoMainActivity;
    ReminderPresenterInterface presenter;
    ProgressDialog progressDialog;
    String userId;
    String currentDate;
    Format format;

    public ReminderFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes){
        this.toDoMainActivity=toDoMainActivity;
        this.allNotes=allNotes;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_remainder, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.reminder_recyclerview);
        presenter=new ReminderPresenter(getActivity(),this);
        progressDialog=new ProgressDialog(getActivity());
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        format = new SimpleDateFormat("MMMM dd,yyyy");
        currentDate = format.format(new Date().getTime());
        //presenter.showDialog("Loading...");
        models = new ArrayList<>();

        presenter.getReminderNoteList(userId);
        checkLayout();

        //presenter.hideDialog();
        recyclerAdapter=new RecyclerAdapter(getActivity().getBaseContext());
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
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        }
    }

    @Override
    public void getReminderNotesSuccess(List<NotesModel> notesModelList) {
        presenter.getReminderNotesSuccess(notesModelList);
    }

    @Override
    public void getNotesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        if(!getActivity().isFinishing()&&progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
