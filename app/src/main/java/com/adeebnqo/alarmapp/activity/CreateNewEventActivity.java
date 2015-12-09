package com.adeebnqo.alarmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.fragment.DurationFragment;
import com.adeebnqo.alarmapp.fragment.NameFragment;
import com.adeebnqo.alarmapp.fragment.TimeFragment;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import com.android.alarmclock.Alarm;
import com.android.alarmclock.Alarms;

public class CreateNewEventActivity extends FragmentActivity implements NameFragment.OnNameGivenListener, TimeFragment.onTimeSelectListener, DurationFragment.OnDurationEnteredListener {

    private Toolbar toolbar;
    private Alarm savedAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        setupToolbar();

        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra(BundleExtras.Event_ID.toString())){
            Bundle bundle = startingIntent.getExtras();
            int eventId = bundle.getInt(BundleExtras.Event_ID.toString());
            savedAlarm = CustomAlarms.getAlarm(eventId);
        }
        showNameFragment();
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.create_new_event_toolbar);
        toolbar.setTitle(getString(R.string.events));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showNameFragment(){
        toolbar.setTitle(getString(R.string.event_name_lc));

        NameFragment fragment = new NameFragment();

        if (savedAlarm!=null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleExtras.Event_OBJECT.toString(), savedAlarm);

            fragment.setArguments(bundle);
        }

        showFragment(fragment);
    }

    private void showDurationFragment(){
        toolbar.setTitle(getString(R.string.duration_lc));

        DurationFragment fragment = new DurationFragment();

        if (savedAlarm!=null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleExtras.Event_OBJECT.toString(), savedAlarm);

            fragment.setArguments(bundle);
        }

        showFragment(fragment);
    }

    private void showTimeFragment(){
        toolbar.setTitle(getString(R.string.time_lc));
        TimeFragment fragment = new TimeFragment();

        if (savedAlarm!=null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleExtras.Event_OBJECT.toString(), savedAlarm);

            fragment.setArguments(bundle);
        }

        showFragment(fragment);
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onGetName(String eventName) {
        if (savedAlarm==null){
            savedAlarm = new Alarm();
        }
        savedAlarm.label = eventName;
        showTimeFragment();
    }

    @Override
    public void backPressedOnTime() {
        showNameFragment();
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        savedAlarm.hour = hour; savedAlarm.minutes = minute;
        showDurationFragment();
    }

    @Override
    public void onBackPressedInDurationScreen() {
        showTimeFragment();
    }

    @Override
    public void onDurationProvided(int ringerMode, int duration) {
        savedAlarm.ringerMode = ringerMode;
        savedAlarm.duration = duration;

        int identifier = Alarms.getAlarmsCursor(getContentResolver()).getCount()+1;
        if (savedAlarm.id == -1) {
            savedAlarm.id = identifier;
        }

        savedAlarm.enabled = true;

        Intent intent = new Intent();
        intent.putExtra(BundleExtras.Event_OBJECT.toString(), savedAlarm);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof TimeFragment){
            backPressedOnTime();
        } else if (currentFragment instanceof DurationFragment){
            onBackPressedInDurationScreen();
        }
        else if (currentFragment instanceof NameFragment){
            super.onBackPressed();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
