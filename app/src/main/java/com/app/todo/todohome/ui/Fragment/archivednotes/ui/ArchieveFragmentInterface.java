package com.app.todo.todohome.ui.Fragment.archivednotes.ui;

import com.app.todo.model.NotesModel;
import java.util.List;

public interface ArchieveFragmentInterface {
    void getNotesSuccess(List<NotesModel> notesModelList);
    void getNotesFailure(String message);

    void showDialog(String message);
    void hideDialog();

    void moveToTrashSuccess(String message);

    void retriveNoteSuccess(String message);
}

