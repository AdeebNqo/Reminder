package com.adeebnqo.alarmapp.scheduling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.List;
import com.adeebnqo.alarmapp.loaders.DatabaseTypeLoader;
import com.adeebnqo.alarmapp.models.Event;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Scheduler scheduler = Scheduler.getInstance();
            List<Event> eventList = DatabaseTypeLoader.getInstance().getDatabase().getStoredEvents();
            for (Event event : eventList){
                if (event.isActive()){
                    scheduler.schedule(event);
                }
            }
        }
    }
}
