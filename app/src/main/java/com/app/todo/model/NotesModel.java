package com.app.todo.model;

public class NotesModel
{
    int id;
    String title;
    String description;
    String noteDate;
    String reminderDate;
    boolean archieve;



    public NotesModel(int id, String title, String description)
    {
        this.id=id;
        this.title=title;
        this.description=description;
    }

    public NotesModel() {

    }

    public NotesModel(String noteDate,String title, String description,String reminderDate,boolean archieve) {
        this.noteDate=noteDate;
        this.title=title;
        this.description=description;
        this.reminderDate=reminderDate;
        this.archieve=archieve;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean isArchieve() {
        return archieve;
    }

    public void setArchieve(boolean archieve) {
        this.archieve = archieve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
