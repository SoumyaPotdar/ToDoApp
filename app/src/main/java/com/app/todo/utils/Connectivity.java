package com.app.todo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by bridgeit on 9/5/17.
 */

public class Connectivity {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connMgr.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

}