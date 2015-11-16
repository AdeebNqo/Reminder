package com.adeebnqo.alarmapp.scheduling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.AppNotification;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.android.alarmclock.Alarm;

public class ScheduleReceiver extends BroadcastReceiver {

    private AudioManager audioManager;
    private Context context;
    private int minOffset;
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    public ScheduleReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        minOffset = context.getResources().getInteger(R.integer.minute_offset);

        if (Math.abs(minOffset) > 50 || minOffset < 0) {
            minOffset = 0;
        }

        if (audioManager == null) {
            audioManager = (AudioManager) ApplicationData.getContext().getSystemService(Context.AUDIO_SERVICE);
        }

        int oldRingerMode = audioManager.getRingerMode();

        int eventId = intent.getExtras().getInt(BundleExtras.Event_ID.toString());
        Alarm event = CustomAlarms.getAlarm(eventId);

        if (event!=null && event.enabled) {

            int duration = event.duration * 60000;
            int ringerType = event.ringerMode;
            Set<Integer> days = new HashSet<>();//TODO : event.getDays();

            //if the ringer is currently the one we need to change it to
            //there is no need to do anything
            if (oldRingerMode != ringerType) {

                Calendar rawScheduledTime = event.getRawTimeAsCalender();
                rawScheduledTime.add(Calendar.MINUTE, -1 * minOffset);
                Date eventScheduledTime = rawScheduledTime.getTime();;
                Date currentTime = Calendar.getInstance().getTime();

                long timeDiff = currentTime.getTime() - eventScheduledTime.getTime();
                int minDiff = (int) (timeDiff % (1000 * 60 * 60));

                if (minDiff >= 0 && minDiff <= duration+minOffset) {

                    Log.d("foobar", "Event is within time constraints.");

                    if (days.isEmpty()) {

                        switchMode(event);
                        resetWhenTimedOut(oldRingerMode, event);
                        CustomAlarms.deactivateAlarm(event);

                        Log.d("foobar", "Event will not be used again automatically.");

                    } else {

                        Calendar today = Calendar.getInstance();
                        int day = today.get(Calendar.DAY_OF_WEEK);
                        if (days.contains(day)) {
                            switchMode(event);
                            //the switch is scheduled for today
                            resetWhenTimedOut(oldRingerMode, event);
                        }
                    }
                } else {
                    Log.d("foobar", "Event is outside time constraints.");
                }
            }
        } else {
            Log.d("foobar", "1. None active event found.");
        }
    }

    private void switchMode(Alarm event) {

        int ringerType = event.ringerMode;
        String eventName = event.label;

        audioManager.setRingerMode(ringerType);
        if (context!=null) {

            String ringerModeText = "";
            switch (ringerType) {
                case AudioManager.RINGER_MODE_NORMAL:
                    ringerModeText = context.getString(R.string.normal_ringer_type);
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    ringerModeText = context.getString(R.string.mute_ringer_type);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    ringerModeText = context.getString(R.string.vibrate_ringer_type);
                    break;
            }

            String notifBody = context.getString(R.string.ringer_is_now, ringerModeText);
            AppNotification.getNotifier().sendNotification(eventName, notifBody);
        }
    }

    private void resetWhenTimedOut(int oldRingerMode, final Alarm alarm) {

        int period = alarm.duration;

        //clone event -- kind of. Creating an event for old ringer mode since
        //we're resetting it the ringer mode
        final Alarm clonedAlarm = new Alarm();
        clonedAlarm.ringerMode = oldRingerMode;
        clonedAlarm.label = alarm.label;

        Runnable task = new Runnable() {
            public void run() {
                switchMode(clonedAlarm);
            }
        };

        worker.schedule(task, period + minOffset, TimeUnit.MINUTES);

    }
}
