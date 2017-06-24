package com.app.todo.todohome.ui.Fragment.notes.interactor;

import com.app.todo.model.NotesModel;

import java.util.List;

public interface NotesInteractorInterface {
     void deleteNote(NotesModel notesModel);
    void archiveNote(NotesModel notesModel);
    void undoNote(NotesModel notesModel);

    void putdata(int index, NotesModel notesModel);

    void moveToTrash(NotesModel notesModel);

    void updateSrNo(List<NotesModel> notesModelList);
}
