package com.app.todo.todohome.ui.Fragment.Remindernotes.interactor;

import android.content.Context;

import com.app.todo.model.NotesModel;
import com.app.todo.todohome.ui.Fragment.Remindernotes.presenter.ReminderPresenter;
import com.app.todo.todohome.ui.Fragment.Remindernotes.presenter.ReminderPresenterInterface;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReminderInteractor implements ReminderInteractorInterface {
    Context context;
    ReminderPresenterInterface presenter;
    List<NotesModel> allNotes;
    String currentDate;
    Format format;

    public ReminderInteractor(Context context, ReminderPresenterInterface presenter) {
    this.context=context;
    this.presenter=presenter;
    }

    @Override
    public void getReminderNoteList(String userId) {
        presenter.showDialog("Loading data");
        format = new SimpleDateFormat("MMMM dd,yyyy");
        currentDate = format.format(new Date().getTime());

        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (note.getReminderDate().equals(currentDate)&& !note.isArchieve()) {
                notesModels.add(note);
            }
            presenter.getReminderNotesSuccess(notesModels);
            presenter.hideDialog();
        }
    }
}
