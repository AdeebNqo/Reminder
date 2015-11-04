package com.adeebnqo.alarmapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by adeeb on 5/19/15.
 */
public class ToastUtil {
    private static Context applicationContext;
    public static void setContext(Context context){
        applicationContext = context;
    }
    public static void showAppMsg(String msg){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show();
    }
}
