package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.activity.intro.Introduction;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.loaders.DatabaseTypeLoader;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.Constants;

public class LandingActivityLoader extends Activity {

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

        }else{

            /*

            If this is not the first time
            the app has ben opened, do not show
            application setup/intro walkthrough


            We need to check if the Database Loader
            does not have a cached database object.
            This is because in the case of ormlite,
            loading retrieving the database takes
            time therefore we cannot reload when
            it's not neccessary to do so.

             */
            if (DatabaseTypeLoader.isNullified()){

                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.activity_loader);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startMainScreen();
                    }
                }).start();

            }else{
                startMainScreen();
            }

        }
    }

    public boolean isIntroBeenShown(){
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILENAME, MODE_PRIVATE);
        boolean introShown = settings.getBoolean(BundleExtras.INTRO_SHOWN.toString(), true);

        return introShown;
    }

    public void startMainScreen(){
        DataProvider dataProvider = DatabaseTypeLoader.getInstance().retrieveDatabase();

        //determining if there are saved ringer change schedules
        if (dataProvider.hasStoredEvents()){
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
        startActivity(intent);
        finish();
    }
}
