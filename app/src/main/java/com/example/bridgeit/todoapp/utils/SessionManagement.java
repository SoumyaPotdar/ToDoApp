package com.example.bridgeit.todoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.bridgeit.todoapp.model.UserModel;

public class SessionManagement {

    SharedPreferences userPref;
    Editor userEditor;
    Context context;

    public SessionManagement(Context context){
        this.context = context;
        userPref = this.context.getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        userEditor = userPref.edit();
    }

    public void  register(UserModel userModel){
        userEditor.putString(Constants.keyname, userModel.getFullname());
        userEditor.putString(Constants.keyemail, userModel.getEmail());
        userEditor.putString(Constants.keymobileno, userModel.getMobileNo());
        userEditor.putString(Constants.keypassword, userModel.getPassword());
        userEditor.putBoolean(Constants.is_login, true);
        userEditor.commit();
    }

    public boolean isLogin(){
        return userPref.getBoolean(Constants.is_login,false);
    }

    public void logout(){
        userEditor.putBoolean(Constants.is_login,false);
        userEditor.commit();

    }

}
