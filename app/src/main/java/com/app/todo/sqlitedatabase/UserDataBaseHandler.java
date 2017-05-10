package com.app.todo.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.todo.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDataBaseHandler extends SQLiteOpenHelper{

    final  static int DATABASE_VERSION = 1;
    private  static  final String DATABASE_NAME="UserdataManager";
    private static final String TABLE_USERS="Users";
    private static final String key_id="id";
    private static final String key_name="name";
    private  static  final String  key_email="email";
    private  static  final String key_mobileno="mobileno";
    private  static  final String key_password="password";



    public UserDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //"CREATE TABLE "+ TABLE_NOTES + "("+ key_id +" TEXT,"+ key_title+" TEXT,"+ key_description+" TEXT)";
        String CREATE_TABLE_USERS="CREATE TABLE "+ TABLE_USERS + "("+ key_id+" TEXT,"+ key_name +" TEXT,"+ key_email+" TEXT,"+ key_mobileno+" TEXT,"+ key_password+" TEXT)";
        db.execSQL(CREATE_TABLE_USERS);
    }

    //upgrading table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_USERS);
        //create table again
        onCreate(db);
    }

    // code to add the new notes
    public  void addUserData(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(key_name,userModel.getFullname());
        values.put(key_email, userModel.getEmail());
        values.put(key_mobileno, userModel.getMobileNo());
        values.put(key_password, userModel.getPassword());


        //inserting rows
        db.insert(TABLE_USERS,null,values);
        db.close();
    }

    // code to update the single note
    public void updateUserData(UserModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();;
        values.put(key_name,model.getFullname());
        values.put(key_email, model.getEmail());
        values.put(key_mobileno, model.getMobileNo());
        values.put(key_password,model.getPassword());

        // updating row
        db.update(TABLE_USERS, values, key_name + " = ?", new String[] { model.getFullname() });
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
    public List<UserModel> getAllNotes() {
        List<UserModel> userModelList= new ArrayList<UserModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setFullname(cursor.getString(0));
                userModel.setEmail(cursor.getString(1));
                userModel.setMobileNo(cursor.getString(2));
                userModel.setPassword(cursor.getString(3));


                // Adding contact to list
                userModelList.add(userModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userModelList;
    }




    public void deleteNote(UserModel userModel) {
        Log.i("abc", "deleteNote: ");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, key_name + " = ?",
                new String[] {userModel.getFullname()});
        db.close();
    }
}
