package com.adeebnqo.alarmapp.activity.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import com.adeebnqo.alarmapp.activity.EventListActivity;
import com.adeebnqo.alarmapp.activity.LandingActivityLoader;
import com.adeebnqo.alarmapp.fragment.intro.FirstSlide;
import com.adeebnqo.alarmapp.fragment.intro.SecondSlide;
import com.adeebnqo.alarmapp.fragment.intro.ThirdSlide;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.adeebnqo.alarmapp.utils.Constants;

public class Introduction extends AppIntro {
    @Override
    public void init(Bundle bundle) {
        addSlide(new FirstSlide(), getApplicationContext());
        addSlide(new SecondSlide(), getApplicationContext());
        addSlide(new ThirdSlide(), getApplicationContext());

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
        startApp();
    }

    @Override
    public void onDonePressed() {
        startApp();
    }

    private void startApp(){

        setIntroShown();

        setResult(RESULT_OK);
        finish();
    }

    private void setIntroShown() {
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.putBoolean(BundleExtras.INTRO_SHOWN.toString(), true);
        settingsEditor.apply();
    }

}
