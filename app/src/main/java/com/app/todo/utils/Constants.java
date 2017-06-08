package com.app.todo.utils;

public class Constants {
    public static String key_pref ="Reg_Pref";
    public static String keyname="namekey";
    public static String keymobileno="mobilenokey";
    public static String keyemail="emailkey";
    public static String keypassword="passwordkey";
    public static String value="No_value";
    public static int splash_Timeout=500;
    public static String mobilepattern="^(\\+91-|\\+91|0)?[7-9]{1}([0-9]){9}$";
    public static String email_pattern="^[a-zA-Z0-9]{3,}@([a-z]){3,}\\.[a-z]+$";
    public static String preferencekey="splashPreference";
    public static final String is_login = "isLogin";
    public static String fb_firstName ="first_name";
    public static String fb_lastName ="last_name";
    public static String facebookLogin;
    public static String profileURL;
    public static final String FIREBASE_URL = "https://todoapp-bece4.firebaseio.com/";
    public static final String keyUserId = "userUid";

    public static final String key_firebase_userData = "userdata";
    public static String is_google_login = "googleUser";
    public static String is_fb_login = "fbUser";
    public static String key_fb_email = "email";

    public interface BundleKey {

        String USER_PROFILE_LOCAL="userProfileLocal";
        String USER_PROFILE_SERVER="userProfileServer";
    }
}
