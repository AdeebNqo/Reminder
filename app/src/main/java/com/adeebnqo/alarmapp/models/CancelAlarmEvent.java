package com.adeebnqo.alarmapp.models;

import com.android.alarmclock.Alarm;

public class CancelAlarmEvent {
    public Alarm alarm;

    public CancelAlarmEvent(Alarm randomAlarm){
        alarm = randomAlarm;
    }
}
