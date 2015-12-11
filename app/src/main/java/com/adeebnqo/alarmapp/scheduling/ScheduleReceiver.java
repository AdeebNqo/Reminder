package com.adeebnqo.alarmapp.scheduling;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import com.adeebnqo.alarmapp.models.CancelAlarmEvent;
import com.adeebnqo.alarmapp.models.HandleAlarmEvent;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.android.alarmclock.Alarm;
import de.greenrobot.event.EventBus;

public class ScheduleReceiver {

    private AudioManager audioManager;
    int oldRingerMode;
    ScheduledFuture<?> scheduledFuture;
    private SharedPreferences data;

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    public ScheduleReceiver(Context context) {
        data = context.getSharedPreferences("com.adeebnqo.alarmapp.scheduling.SchedulingData", Context.MODE_PRIVATE);
        if (audioManager == null) {
            audioManager = (AudioManager) ApplicationData.getContext().getSystemService(Context.AUDIO_SERVICE);
        }
        EventBus.getDefault().register(this);
    }



    public void onEvent(HandleAlarmEvent alarmEvent) {
        oldRingerMode = audioManager.getRingerMode();
        data.edit().putInt("old_ringer_mode", oldRingerMode).apply();
        switchMode(alarmEvent.alarm);
        resetWhenTimedOut(alarmEvent.alarm);
    }

    public void onEvent(CancelAlarmEvent cancelAlarmEvent) {
        if (!scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
            cancelAlarmEvent.alarm.duration = 0;
            resetWhenTimedOut(cancelAlarmEvent.alarm);
        }
    }

    private void switchMode(Alarm event) {
        int ringerType = event.ringerMode;
        audioManager.setRingerMode(ringerType);
    }

    private void resetWhenTimedOut(final Alarm alarm) {

        oldRingerMode  = data.getInt("old_ringer_mode", 0);
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

        scheduledFuture = worker.schedule(task, period, TimeUnit.MINUTES);
    }
}
