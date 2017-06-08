package com.app.todo.todohome.ui.Fragment.archivednotes.interactor;

import com.app.todo.model.NotesModel;

public interface ArchiveInteractorInterface {
    void getAllNotelist(String userId);

    void moveToTrash(NotesModel notesModel);

    void retriveNote(NotesModel notesModel);
}
