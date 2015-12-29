package com.adeebnqo.alarmapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.android.alarmclock.Alarm;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

public class CalendarSyncService extends Service {

    public static final String ACTION_SYNC = "com.adeebnqo.alarmapp.services.CalendarSyncReceiver.ACTION_SYNC";
    public Handler handler = null;
    public static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                int delay = Integer.valueOf(prefs.getString("sync_frequency", "-1"));
                syncEvents(CalendarSyncService.this);
                if (delay != -1) {
                    handler.postDelayed(runnable, TimeUnit.MINUTES.toMillis(delay));
                }
            }
        };

        runnable.run();
    }

    public void syncEvents(Context context) {

        CalendarProvider provider = new CalendarProvider(context);
        List<Calendar> calendars = provider.getCalendars().getList();

        Iterator<Calendar> calendarIterator = calendars.iterator();
        while(calendarIterator.hasNext()) {
            Calendar calendar = calendarIterator.next();
            List<Event> events = provider.getEvents(calendar.id).getList();
            for (Event event : events) {
                if (!event.allDay && !CustomAlarms.hasAlarm((int)(event.id))) {

                    Date lastDate = new Date(event.lastDate);
                    Date endDate = new Date(event.dTend);
                    Date nowDate = java.util.Calendar.getInstance().getTime();

                    if (endDate.before(nowDate) && lastDate.after(nowDate)) {

                        Alarm newAlarm = new Alarm();
                        newAlarm.enabled = true;
                        newAlarm.label = event.title;
                        newAlarm.id = (int)event.id;

                        long duration = new Date(event.dTend - event.dTStart).getTime() / 60000;
                        newAlarm.duration = (int) duration;

                        Date startTime = new Date(event.dTStart);
                        java.util.Calendar startTimeCalendar = java.util.Calendar.getInstance();
                        startTimeCalendar.setTime(startTime);

                        newAlarm.minutes = startTimeCalendar.get(java.util.Calendar.MINUTE);
                        newAlarm.hour = startTimeCalendar.get(java.util.Calendar.HOUR_OF_DAY);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        int defaultRingerMode = prefs.getInt("default_ringermode", AudioManager.RINGER_MODE_VIBRATE);
                        newAlarm.ringerMode = defaultRingerMode;

                        CustomAlarms.addAlarm(newAlarm);

                    }
                }
            }
        }
    }
}
