package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.activity.intro.Introduction;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.Constants;
import com.android.alarmclock.Alarms;

public class LandingActivityLoader extends Activity {

    private final int INTRO_CODE = 1;

    boolean introBeenShown;
    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        if (!BuildConfig.IS_DEBUG) {
            ((SwitchRinger) getApplication()).getTracker();
        }

        introBeenShown = isIntroBeenShown();

        if (!introBeenShown){

            showIntro();

        } else {

            /*

            If this is not the first time
            the app has ben opened, do not show
            application setup/intro walkthrough

             */

            startMainScreen();

        }
    }

    public boolean isIntroBeenShown(){
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILENAME, MODE_PRIVATE);
        return settings.getBoolean(BundleExtras.INTRO_SHOWN.toString(), true);
    }

    public void startMainScreen(){
        Cursor cursor = Alarms.getAlarmsCursor(getContentResolver());

        //determining if there are saved ringer change schedules
        if (cursor.getCount() > 0){
            //open activity with list
            Intent MainListStarter = new Intent(this, EventListActivity.class);
            startActivity(MainListStarter);
        }
        else{
            //open activity with text saying that there is nothing yet.
            Intent emptyListStarter = new Intent(this, NoEventsActivity.class);
            startActivity(emptyListStarter);
        }
        finish();
    }

    public void showIntro(){
        Intent intent = new Intent(LandingActivityLoader.this, Introduction.class);
        startActivityForResult(intent, INTRO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTRO_CODE) {
            startMainScreen();
        }
    }
}
