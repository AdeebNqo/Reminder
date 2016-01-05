package com.adeebnqo.alarmapp.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.scheduling.ScheduleReceiver;
import com.adeebnqo.alarmapp.utils.AnalyticsManager;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.AppNotification;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.adeebnqo.alarmapp.utils.Constants;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import io.fabric.sdk.android.Fabric;



public class SwitchRinger extends Application {

    private static Tracker tracker = null;
    private ScheduleReceiver scheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        //poor man's dependency injection -- injecting application context
        ToastUtil.setContext(getApplicationContext());
        ApplicationData.setContext(getApplicationContext());
        AppNotification.Init(getApplicationContext());
        CustomAlarms.Init(getApplicationContext());
        scheduler = new ScheduleReceiver(getApplicationContext());
        AnalyticsManager.init(this);

        //Intro screens
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILENAME, MODE_PRIVATE);
        if (!settings.contains(BundleExtras.INTRO_SHOWN.toString())){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(BundleExtras.INTRO_SHOWN.toString(), false);
            editor.apply();
        }
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.enableAutoActivityReports(this);
            tracker = analytics.newTracker(R.xml.analytics);
            tracker.enableAdvertisingIdCollection(true);
        }
        return tracker;
    }
}
