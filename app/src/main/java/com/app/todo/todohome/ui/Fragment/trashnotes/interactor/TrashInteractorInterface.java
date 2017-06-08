package com.app.todo.todohome.ui.Fragment.trashnotes.interactor;

import com.app.todo.model.NotesModel;

public interface TrashInteractorInterface {
    void getAllNotelist(String userId);
    void deleteNote(int position, NotesModel notesModel);
    void retriveNote(NotesModel notesModel);

}
