package com.adeebnqo.alarmapp.loaders;

import android.content.Context;
import android.database.Cursor;
import com.android.alarmclock.Alarm;
import com.android.alarmclock.Alarms;

import java.util.ArrayList;
import java.util.List;

public class CustomAlarms {
    public static Context appContext;

    public static void Init(Context context){
        appContext = context;
    }

    public static List<Alarm> getAlarms(){
        List<Alarm> result = new ArrayList<>();

        Cursor cursor = Alarms.getAlarmsCursor(appContext.getContentResolver());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            result.add(new Alarm(cursor));
            cursor.moveToNext();
        }

        return result;
    }

    public static Alarm getAlarm(int id){
        return Alarms.getAlarm(appContext.getContentResolver(), id);
    }

    public static void deleteAlarm(Alarm someAlarm){
        Alarms.deleteAlarm(appContext, someAlarm.id);
    }

    public static void deactivateAlarm(Alarm someAlarm){
        someAlarm.enabled = false;
        Alarms.deleteAlarm(appContext, someAlarm.id);
        addAlarm(someAlarm);
    }

    public static void activateAlarm(Alarm someAlarm){
        someAlarm.enabled = true;
        Alarms.deleteAlarm(appContext, someAlarm.id);
        addAlarm(someAlarm);
    }

    public static void addAlarm(Alarm someAlarm){
        Alarms.setAlarm(appContext, someAlarm.id, someAlarm.enabled, someAlarm.hour, someAlarm.minutes, someAlarm.daysOfWeek, false, someAlarm.label, someAlarm.label, someAlarm.duration);

    }

    public  static boolean hasAlarms(){
        return getAlarms().size() > 0;
    }
}
