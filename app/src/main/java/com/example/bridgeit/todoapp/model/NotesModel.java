package com.example.bridgeit.todoapp.model;

public class NotesModel
{
    String title;
    String description;
    int id;

    public NotesModel(String title, String description)
    {
        this.title=title;
        this.description=description;
    }

    public NotesModel() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
