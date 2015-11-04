package com.adeebnqo.alarmapp.scheduling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScheduleResetReceiver extends BroadcastReceiver {

    public static String RESENT_ALARM_SCHEDULE_ACTION = "com.adeebnqo.alarmapp.scheduling.intent.action.RESET_SCHEDULE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(RESENT_ALARM_SCHEDULE_ACTION)){

        }
    }
}
