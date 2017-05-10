package com.app.todo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.app.todo.model.UserModel;

public class SessionManagement {

    SharedPreferences userPref;
    Editor userEditor;
    Context context;

    public SessionManagement(Context context){
        this.context = context;
        userPref = this.context.getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        userEditor = userPref.edit();
    }

    public void loginToSharedPref(UserModel userModel, boolean is_google_login,
                                  boolean is_fb_login){
        userEditor.putString(Constants.keyname, userModel.getFullname());
        userEditor.putString(Constants.keyemail, userModel.getEmail());
        userEditor.putString(Constants.keymobileno, userModel.getMobileNo());
        userEditor.putString(Constants.keypassword, userModel.getPassword());
        userEditor.putString(Constants.keyUserId, userModel.getId());
        userEditor.putBoolean(Constants.is_login, true);
        userEditor.putBoolean(Constants.is_google_login, is_google_login);
        userEditor.putBoolean(Constants.is_fb_login, is_fb_login);

        userEditor.commit();
    }

    public UserModel getUserDetails(){
        UserModel user = new UserModel();
        user.setFullname(userPref.getString(Constants.keyname,""));
        user.setEmail(userPref.getString(Constants.keyemail,""));
        user.setPassword(userPref.getString(Constants.keypassword,""));
        user.setMobileNo(userPref.getString(Constants.keymobileno,""));
        user.setId(userPref.getString(Constants.keyUserId,""));
        return user;
    }

    public boolean isFbLogin(){
        return userPref.getBoolean(Constants.is_fb_login, false);
    }

    public boolean isGoogleLogin(){
        return userPref.getBoolean(Constants.is_google_login, false);
    }

    public boolean isLogin(){
        return userPref.getBoolean(Constants.is_login,false);
    }

    public void logout(){
        userEditor.clear();
        userEditor.putBoolean(Constants.is_google_login,false);
        userEditor.putBoolean(Constants.is_fb_login,false);
        userEditor.putBoolean(Constants.is_login,false);
        userEditor.putBoolean(Constants.is_login,false);
        userEditor.commit();


    }

}
