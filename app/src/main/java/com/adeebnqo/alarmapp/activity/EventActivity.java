package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.BitSet;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import com.android.alarmclock.Alarm;

public class EventActivity extends Activity implements View.OnClickListener{

    private static int EVENT_SCREEN_CODE = 1;

    private ImageView headerImage;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private TextView duration;
    private TextView startTime;
    private Alarm chosenAlarm;

    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private ToggleButton sunday;

    int deactivatedColor;
    int activatedColor;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        if (intent.hasExtra(BundleExtras.Event_ID.toString())){
            int NOT_FOUND_CONSTANT = Integer.MAX_VALUE;
            int identifier = intent.getIntExtra(BundleExtras.Event_ID.toString(), NOT_FOUND_CONSTANT);
            if (identifier!=NOT_FOUND_CONSTANT){
                chosenAlarm = CustomAlarms.getAlarm(identifier);
            }
            if (chosenAlarm ==null){
                returnToEventList();
            } else {

                deactivatedColor = getResources().getColor(R.color.secondary_text);
                activatedColor = getResources().getColor(R.color.accent);

                setupFab();
                setupToolbar();
                setupHeaderImage();

                renderEventDetails();
            }
        } else {
            returnToEventList();
        }

    }

    private void setupToolbar(){

        toolbar = (Toolbar) findViewById(R.id.event_detail_tooolbar);
        toolbar.inflateMenu(R.menu.event_detail);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        Drawable mDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mDrawable = getDrawable(R.drawable.ic_arrow_back);
        } else {
            mDrawable = getResources().getDrawable(R.drawable.ic_arrow_back);
        }
        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
        }
        toolbar.setNavigationIcon(mDrawable);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventActivity.this.onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_event: {
                        Intent intent = new Intent(EventActivity.this, CreateNewEventActivity.class);
                        intent.putExtra(BundleExtras.Event_ID.toString(), chosenAlarm.id);
                        startActivityForResult(intent, EVENT_SCREEN_CODE);
                        break;
                    }
                    case R.id.action_delete_event: {
                        progress = progress.show(EventActivity.this, getString(R.string.delete_action),
                                getString(R.string.please_wait), true);
                        CustomAlarms.deleteAlarm(chosenAlarm);
                        progress.dismiss();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });

    }
    private void setupHeaderImage(){
        headerImage = (ImageView) findViewById(R.id.header_img);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(chosenAlarm.label);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) + 30 >= appBarLayout.getTotalScrollRange()) {
                    //closed
                    if (fab.getVisibility() != View.GONE) {
                        fab.setVisibility(View.GONE);
                    }
                } else {
                    //open
                    if (fab.getVisibility() != View.VISIBLE) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    private void setupFab(){
        fab = (FloatingActionButton) findViewById(R.id.event_detail_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosenAlarm.enabled) {
                    CustomAlarms.deactivateAlarm(chosenAlarm);
                } else {
                    CustomAlarms.activateAlarm(chosenAlarm);
                }
                drawIconOnFab();
            }
        });

        //initializing state of icon in FAB
        drawIconOnFab();
    }

    private void drawIconOnFab(){
        if (!chosenAlarm.enabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_inactive, getApplicationContext().getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_inactive));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_active,  getApplicationContext().getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_active));
            }
        }
    }

    private void renderEventDetails(){
        duration = (TextView) findViewById(R.id.val_duration);
        startTime = (TextView) findViewById(R.id.val_time);

        duration.setText("" + chosenAlarm.duration);
        startTime.setText(chosenAlarm.getFormattedTime());


        //loading day-repeat buttons
        monday  = (ToggleButton) findViewById(R.id.button_monday); monday.setOnClickListener(this);
        tuesday = (ToggleButton) findViewById(R.id.button_tuesday); tuesday.setOnClickListener(this);
        wednesday = (ToggleButton) findViewById(R.id.button_wednesday); wednesday.setOnClickListener(this);
        thursday = (ToggleButton) findViewById(R.id.button_thursday); thursday.setOnClickListener(this);
        friday = (ToggleButton) findViewById(R.id.button_friday); friday.setOnClickListener(this);
        saturday = (ToggleButton) findViewById(R.id.button_saturday); saturday.setOnClickListener(this);
        sunday = (ToggleButton) findViewById(R.id.button_sunday); sunday.setOnClickListener(this);
        ToggleButton[] dayButtons = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};

        boolean[] setDays  = chosenAlarm.daysOfWeek.getBooleanArray();
        for (int i=0; i<setDays.length; ++i) {
            dayButtons[i].setActivated(setDays[i]);
        }
    }

    private void returnToEventList(){
        ToastUtil.showAppMsg(getString(R.string.invalid_event));
        Intent redirect = new Intent(EventActivity.this, EventListActivity.class);
        startActivity(redirect);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ToggleButton){
            ToggleButton butt = (ToggleButton) view;
            butt.setActivated(!butt.isActivated());

            int codedDays = chosenAlarm.daysOfWeek.getCoded();
            BitSet daysOfWeekBits;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                daysOfWeekBits = BitSet.valueOf(new long[]{codedDays});
            } else {
                daysOfWeekBits = getBitSet(codedDays);
            }

            int buttonId = butt.getId();
            switch (buttonId){
                case R.id.button_monday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(0);
                    } else {
                        daysOfWeekBits.clear(0);
                    }
                    break;
                }
                case R.id.button_tuesday:{
                    if (butt.isActivated()){
                        daysOfWeekBits.set(1);
                    } else {
                        daysOfWeekBits.clear(1);
                    }
                    break;
                }
                case R.id.button_wednesday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(2);
                    } else {
                        daysOfWeekBits.clear(2);
                    }
                    break;
                }
                case R.id.button_thursday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(3);
                    } else {
                        daysOfWeekBits.clear(3);
                    }
                    break;
                }
                case R.id.button_friday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(4);
                    } else {
                        daysOfWeekBits.clear(4);
                    }
                    break;
                }
                case R.id.button_saturday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(5);
                    } else {
                        daysOfWeekBits.clear(5);
                    }
                    break;
                }
                case R.id.button_sunday:{
                    if (butt.isActivated()) {
                        daysOfWeekBits.set(6);
                    } else {
                        daysOfWeekBits.clear(6);
                    }
                    break;
                }
            }

            try {

                CustomAlarms.deleteAlarm(chosenAlarm);
                chosenAlarm.daysOfWeek = new Alarm.DaysOfWeek(getIntFromBitset(daysOfWeekBits));
                CustomAlarms.addAlarm(chosenAlarm);

            } catch(Exception e) {
                ToastUtil.showAppMsg(getString(R.string.internal_error));
                e.printStackTrace();
            }
        }
    }

    /*
     *
     * Arne Burmeister
     * http://stackoverflow.com/a/2473719/1984350
     *
     */
    public static BitSet getBitSet(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    /*
    *
    *Arne Burmeister
    * http://stackoverflow.com/a/2473719/1984350
    *
    */
    public static int getIntFromBitset(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_SCREEN_CODE && resultCode == RESULT_OK){

            Alarm editedEvent  = data.getExtras().getParcelable(BundleExtras.Event_OBJECT.toString());
            editedEvent.daysOfWeek = chosenAlarm.daysOfWeek;
            editedEvent.id = chosenAlarm.id;

            try {

                CustomAlarms.deleteAlarm(chosenAlarm);
                CustomAlarms.addAlarm(editedEvent);

            } catch(Exception e) {
                ToastUtil.showAppMsg(getString(R.string.internal_error));
                e.printStackTrace();
            }

            Intent reloadedIntent = getIntent();
            reloadedIntent.removeExtra(BundleExtras.Event_ID.toString());
            reloadedIntent.putExtra(BundleExtras.Event_ID.toString(), editedEvent.id);
            finish();
            startActivity(reloadedIntent);
        }
    }
}
