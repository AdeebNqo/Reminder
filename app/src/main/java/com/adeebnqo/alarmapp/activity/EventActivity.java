package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Set;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.exceptions.EventUpdateException;
import com.adeebnqo.alarmapp.managers.EventManager;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import com.squareup.picasso.Picasso;

public class EventActivity extends Activity implements View.OnClickListener{

    private static int EVENT_SCREEN_CODE = 1;

    private ImageView headerImage;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private TextView duration;
    private TextView startTime;
    TextView activityTitle;

    private Event chosenEvent;
    private EventManager eventManager;

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

        eventManager = EventManager.getInstance();

        try{

            Intent intent = getIntent();
            if (intent.hasExtra(BundleExtras.Event_ID.toString())){
                int NOT_FOUND_CONSTANT = Integer.MAX_VALUE;
                int identifier = intent.getIntExtra(BundleExtras.Event_ID.toString(), NOT_FOUND_CONSTANT);
                if (identifier!=NOT_FOUND_CONSTANT){
                    chosenEvent = eventManager.getEvent(identifier);
                }
                if (chosenEvent==null){
                    returnToEventList();
                }else{

                    deactivatedColor = getResources().getColor(R.color.secondary_text);
                    activatedColor = getResources().getColor(R.color.accent);

                    setupFab();
                    setupToolbar();
                    setupHeaderImage();

                    renderEventDetails();
                }
            }else{
                returnToEventList();
            }

        }catch(EventNotFoundException e){
            e.printStackTrace();
            returnToEventList();
        }
    }

    private void setupToolbar(){

        toolbar = (Toolbar) findViewById(R.id.event_detail_tooolbar);
        toolbar.inflateMenu(R.menu.event_detail);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
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
                        intent.putExtra(BundleExtras.Event_ID.toString(), chosenEvent.getIdentifier());
                        startActivityForResult(intent, EVENT_SCREEN_CODE);
                        break;
                    }
                    case R.id.action_delete_event: {
                        progress = progress.show(EventActivity.this, getString(R.string.delete_action),
                                getString(R.string.please_wait), true);
                        EventManager.getInstance().removeEvent(chosenEvent);
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
        collapsingToolbarLayout.setTitle(chosenEvent.getName());

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    //closed
                    if (fab.getVisibility() != View.GONE){
                        fab.setVisibility(View.GONE);
                    }
                } else {
                    //open
                    if (fab.getVisibility() != View.VISIBLE){
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //TODO setup time based img/animation
    }
    private void setupFab(){
        fab = (FloatingActionButton) findViewById(R.id.event_detail_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosenEvent.isActive()) {
                    EventManager.getInstance().deActivateEvent(chosenEvent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_inactive, getApplicationContext().getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_inactive));
                    }
                } else {
                    EventManager.getInstance().activateEvent(chosenEvent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_active,  getApplicationContext().getTheme()));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_active));
                    }
                }
            }
        });

        //initializing state of icon in FAB
        if (chosenEvent.isActive()) {
            //TODO fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_active));
        }else{
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_inactive));
        }
    }

    private void renderEventDetails(){
        duration = (TextView) findViewById(R.id.val_duration);
        startTime = (TextView) findViewById(R.id.val_time);

        duration.setText(""+chosenEvent.getDuration());
        startTime.setText(chosenEvent.getFormattedTime());

        if (!chosenEvent.isActive()) {
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


        //loading day-repeat buttons
        monday  = (ToggleButton) findViewById(R.id.button_monday); monday.setOnClickListener(this);
        tuesday = (ToggleButton) findViewById(R.id.button_tuesday); tuesday.setOnClickListener(this);
        wednesday = (ToggleButton) findViewById(R.id.button_wednesday); wednesday.setOnClickListener(this);
        thursday = (ToggleButton) findViewById(R.id.button_thursday); thursday.setOnClickListener(this);
        friday = (ToggleButton) findViewById(R.id.button_friday); friday.setOnClickListener(this);
        saturday = (ToggleButton) findViewById(R.id.button_saturday); saturday.setOnClickListener(this);
        sunday = (ToggleButton) findViewById(R.id.button_sunday); sunday.setOnClickListener(this);

        //setting color of day icons based on click status
        Set<Integer> repeatDays =  chosenEvent.getDays();
        if (null != repeatDays){
            for (Integer day : repeatDays){
                switch(day){
                    case Calendar.MONDAY:{
                        monday.setActivated(true);
                        break;
                    }
                    case Calendar.TUESDAY:{
                        tuesday.setActivated(true);
                        break;
                    }
                    case Calendar.WEDNESDAY:{
                        wednesday.setActivated(true);
                        break;
                    }
                    case Calendar.THURSDAY:{
                        thursday.setActivated(true);
                        break;
                    }
                    case Calendar.FRIDAY:{
                        friday.setActivated(true);
                        break;
                    }
                    case Calendar.SATURDAY:{
                        saturday.setActivated(true);
                        break;
                    }
                    case Calendar.SUNDAY:{
                        sunday.setActivated(true);
                        break;
                    }
                }
            }
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

            Set<Integer> currentSelectedDays = chosenEvent.getDays();

            int buttonId = butt.getId();
            switch (buttonId){
                case R.id.button_monday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.MONDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.MONDAY);
                    }
                    break;
                }
                case R.id.button_tuesday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.TUESDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.TUESDAY);
                    }
                    break;
                }
                case R.id.button_wednesday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.WEDNESDAY);
                    }else{
                        currentSelectedDays.add(Calendar.WEDNESDAY);
                    }
                    break;
                }
                case R.id.button_thursday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.THURSDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.THURSDAY);
                    }
                    break;
                }
                case R.id.button_friday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.FRIDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.FRIDAY);
                    }
                    break;
                }
                case R.id.button_saturday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.SATURDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.SATURDAY);
                    }
                    break;
                }
                case R.id.button_sunday:{
                    if (butt.isActivated()){
                        currentSelectedDays.add(Calendar.SUNDAY);
                    }else{
                        currentSelectedDays.remove(Calendar.SUNDAY);
                    }
                    break;
                }
            }

            //updating days
            try {
                eventManager.updateEventDays(chosenEvent, currentSelectedDays);
            }catch (EventUpdateException e){
                ToastUtil.showAppMsg(getString(R.string.internal_error));
                butt.setActivated(!butt.isActivated());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_SCREEN_CODE && resultCode == RESULT_OK){

            Event editedEvent  = (Event) data.getExtras().get(BundleExtras.Event_OBJECT.toString());
            editedEvent.setDays(chosenEvent.getDays());
            editedEvent.setIdentifier(chosenEvent.getIdentifier());


            try{

                EventManager.getInstance().removeEvent(chosenEvent);
                EventManager.getInstance().addEvent(editedEvent);

            } catch(Exception e) {
                ToastUtil.showAppMsg(getString(R.string.internal_error));
                e.printStackTrace();
            }

            Intent reloadedIntent = getIntent();
            reloadedIntent.removeExtra(BundleExtras.Event_ID.toString());
            reloadedIntent.putExtra(BundleExtras.Event_ID.toString(), editedEvent.getIdentifier());
            finish();
            startActivity(reloadedIntent);
        }
    }
}
