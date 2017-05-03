package com.example.bridgeit.todoapp.model;

public class NotesModel
{
    String noteDate;
    String title;
    String description;
    int id;
    String reminderDate;



    public NotesModel(int id, String title, String description)
    {
        this.id=id;
        this.title=title;
        this.description=description;
    }

    public NotesModel() {

    }

    public NotesModel(String date,String title, String description) {
        this.noteDate=date;
        this.title=title;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return noteDate;
    }

    public void setDate(String date) {
        this.noteDate = date;
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
