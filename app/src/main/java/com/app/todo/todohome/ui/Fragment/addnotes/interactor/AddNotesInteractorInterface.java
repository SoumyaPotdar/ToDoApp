package com.app.todo.todohome.ui.Fragment.addnotes.interactor;

import com.app.todo.model.NotesModel;

public interface AddNotesInteractorInterface {
    void getIndex(final NotesModel notesModel);
    void putdata(int index, NotesModel model);
}
