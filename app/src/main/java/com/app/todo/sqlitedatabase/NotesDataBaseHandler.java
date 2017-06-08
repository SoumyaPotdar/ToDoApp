package com.app.todo.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.todo.model.NotesModel;

import java.util.ArrayList;
import java.util.List;

public class NotesDataBaseHandler extends SQLiteOpenHelper
{

    final  static int DATABASE_VERSION = 1;
    private  static  final String DATABASE_NAME="NotesManager";
    private static final String TABLE_NOTES="Notes";
    private static final String key_id="id";
    private static final String key_currentdate="noteDate";
    private  static  String  key_title="title";
    private  static  final String key_description="description";
    private  static  final String key_reminderdate="reminderDate";
    private  static  final String key_archieve="archieve";
    private  static  final String key_colorpick="colorpick";

    public NotesDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//create table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE_NOTES="CREATE TABLE "+ TABLE_NOTES + "("+ key_id +" TEXT,"+ key_title+" TEXT,"+ key_description+" TEXT,"+ key_currentdate +" TEXT,"+ key_reminderdate+" TEXT,"+ key_archieve+" TEXT,"+key_colorpick+" TEXT)";
        db.execSQL(CREATE_TABLE_NOTES);
    }

    //upgrading table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NOTES);
        //create table again
        onCreate(db);
    }

    
    // code to add the new notes
    public  void addNote(NotesModel notesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_currentdate,notesModel.getNoteDate());
        values.put(key_id,notesModel.getId());
        values.put(key_title, notesModel.getTitle());
        values.put(key_description, notesModel.getDescription());
        values.put(key_reminderdate,notesModel.getReminderDate());
        values.put(key_archieve,notesModel.isArchieve());
        values.put(key_colorpick,notesModel.getColor());

        //inserting rows
        db.insert(TABLE_NOTES,null,values);
        db.close();
    }

    // code to update the single note
    public void updateNotes(NotesModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        /*values.put(key_id,model.getId());*/
        values.put(key_currentdate,model.getNoteDate());
        values.put(key_title, model.getTitle());
        values.put(key_description, model.getDescription());
        values.put(key_reminderdate,model.getReminderDate());
        values.put(key_archieve,model.isArchieve());
        values.put(key_colorpick,model.getColor());

        // updating row
       db.update(TABLE_NOTES, values, key_title + " = ?", new String[] { model.getTitle() });
        Log.i("", "updateNotes: ");
    }

    // code to get the single note
   /*public NotesModel getNotes(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{key_id,key_title, key_description}, key_title = "?", new String[]{title}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        NotesModel notesModel = new NotesModel(cursor.getString(0), cursor.getString(1),cursor.getString(2));
        return notesModel;
    }*/



    // code to get all notes in a list view
    public List<NotesModel> getAllNotes() {
        List<NotesModel> noteslist = new ArrayList<NotesModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NotesModel notesModel = new NotesModel();
                notesModel.setNoteDate(cursor.getString(0));
                notesModel.setTitle(cursor.getString(1));
                notesModel.setDescription(cursor.getString(2));
                notesModel.setReminderDate(cursor.getString(3));
                notesModel.setColor(cursor.getString(4));
/*
                notesModel.setArchieve(cursor.getString(4));
*/
                // Adding contact to list
                noteslist.add(notesModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return noteslist;
    }




    public void deleteNote(NotesModel notesModel) {
        Log.i("abc", "deleteNote: ");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, key_title + " = ?",new String[] {notesModel.getTitle()});
        db.close();
    }
   /* public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }*/

}








