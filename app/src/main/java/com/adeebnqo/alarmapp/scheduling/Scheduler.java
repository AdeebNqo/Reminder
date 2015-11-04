package com.adeebnqo.alarmapp.scheduling;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.utils.ApplicationData;

public class Scheduler {

    private AlarmManager alarmManager;
    private Scheduler() {
        alarmManager = (AlarmManager) ApplicationData.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    private static Scheduler INSTANCE;
    public static Scheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Scheduler();
        }
        return INSTANCE;
    }

    public void schedule(Event scheduleEvent) {

        Context context = ApplicationData.getContext();

        Calendar calNow = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, scheduleEvent.getHour());
        int minOffset = context.getResources().getInteger(R.integer.minute_offset);
        cal.set(Calendar.MINUTE, scheduleEvent.getMinute());
        cal.set(Calendar.SECOND, 0);

        cal.add(Calendar.MINUTE, -1 * minOffset);


        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.setAction("com.adeebnqo.alarmapp.START_ALARM");
        intent.putExtra(BundleExtras.Event_ID.toString(), scheduleEvent.getIdentifier());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleEvent.getIdentifier(), intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    public boolean checkIfAlarmExists(Event scheduleEvent) {

        int alarmId = scheduleEvent.getIdentifier();
        Context context = ApplicationData.getContext();
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.setAction("com.adeebnqo.alarmapp.START_ALARM");
        intent.putExtra(BundleExtras.Event_ID.toString(), alarmId);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_NO_CREATE);

        return pIntent == null;
    }

    public void unSchedule(Event event) {

        Intent intent = new Intent();
        intent.putExtra(BundleExtras.Event_ID.toString(), event.getIdentifier());

        PendingIntent pIntent = PendingIntent.getBroadcast(ApplicationData.getContext(), event.getIdentifier(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
    }
}
