package com.adeebnqo.alarmapp.utils;

import android.app.Application;
import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.activity.SwitchRinger;
import com.google.android.gms.analytics.HitBuilders;

public class AnalyticsManager {

    private Application application;
    private static AnalyticsManager instance;

    public static void init(Application applicationContext) {
        getInstance();
        instance.application = applicationContext;
    }
    public static AnalyticsManager getInstance(){
        if (instance == null) {
            instance = new AnalyticsManager();
        }
        return instance;
    }

    public void sendEvent(String category, String action){
        if (!BuildConfig.IS_DEBUG) {

            ((SwitchRinger) application).getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());
        }
    }
}
