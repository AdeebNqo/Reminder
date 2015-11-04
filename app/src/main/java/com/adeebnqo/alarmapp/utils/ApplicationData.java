package com.adeebnqo.alarmapp.utils;

import android.content.Context;

/**
 * Created by adeeb on 3/8/15.
 */
public class ApplicationData{

    private static Context applicationContext;

    public static void setContext(Context context){
        applicationContext = context;
    }
    public static Context getContext(){
        return applicationContext;
    }
}
