package com.adeebnqo.alarmapp.activity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.activity.intro.Introduction;
import com.adeebnqo.alarmapp.adapters.EventAdapter;
import com.adeebnqo.alarmapp.adapters.NavAdapter;
import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.loaders.DatabaseTypeLoader;
import com.adeebnqo.alarmapp.managers.EventManager;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.models.DataAvailableEvent;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.models.NavigationItem;
import com.adeebnqo.alarmapp.utils.Constants;
import com.adeebnqo.alarmapp.utils.Convertor;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import de.greenrobot.event.EventBus;

public class EventListActivity extends ActionBarActivity{

    public static int EVENT_LIST_SCREEN_CODE = 1;

    private Toolbar toolbar;

    private RecyclerView.LayoutManager layoutManager;
    private EventAdapter eventAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private String aboutTitle;
    private String featureTourTitle;

    private boolean introBeenShown;

    // Screen layers
    private RelativeLayout noEventsLayer;
    private RecyclerView recyclerView;

    //data provider and data
    DataProvider dataProvider;
    private List<Event> eventList;
    private EventBus eventBus;

    private String developerEmail = "androidpot107@gmail.com";

    @Override
    protected void onStop() {
        super.onStop();

        eventBus.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        //removing activity transitions
        //overridePendingTransition(0, 0);

        eventBus = EventBus.getDefault();
        eventBus.register(this);

        aboutTitle = getString(R.string.title_activity_advert);
        featureTourTitle = getString(R.string.product_tour);

        loadLayers();
        setupToolbar();
        setupNavDrawer();

        introBeenShown = isIntroBeenShown();

        if (!introBeenShown){

            //preloading database while the user is looking the app intro
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataProvider = DatabaseTypeLoader.getInstance().getDatabase();
                }
            }).start();

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

                showLoadingScreen();

            }else{
                showEvents();
            }
        }
    }

    private void showEventList(){
        setupToolbar();

        eventList = dataProvider.getStoredEvents();

        Event[] someEvents = new Event[eventList.size()];
        eventAdapter = new EventAdapter(eventList.toArray(someEvents), this);
        recyclerView.setAdapter(eventAdapter);

        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.events));
        setSupportActionBar(toolbar);

        noEventsLayer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    private void showNoEventLayer(){
        setupToolbar();

        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(getString(R.string.no_events));
        setSupportActionBar(toolbar);

        noEventsLayer.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    private void showLoadingScreen() {

        noEventsLayer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        new Thread(new Runnable() {
            @Override
            public void run() {
                showEvents();
            }
        }).start();
    }

    private void showEvents(){
        dataProvider = DatabaseTypeLoader.getInstance().getDatabase();
        if (dataProvider==null){
            ToastUtil.showAppMsg(getString(R.string.internal_error));
            finish();
        }else {

            boolean hasStoredEvents = dataProvider.hasStoredEvents();

            if (hasStoredEvents){
                eventBus.post(new DataAvailableEvent(true));
            }else{
                eventBus.post(new DataAvailableEvent(false));
            }
        }
    }

    public void onEvent(DataAvailableEvent event){
        if (event.isDataAvailable()){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showEventList();
                }
            });
        }else{
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showNoEventLayer();
                }
            });
        }
    }

    private void loadLayers(){
        setupEventList();
        setupNoEvents();
    }

    private void setupNavDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this ,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_product_tour:{
                        showIntro();
                        break;
                    }
                    case R.id.navigation_about_app:{
                        Intent intent = new Intent(EventListActivity.this, AdvertActivity.class);
                        startActivity(intent);
                        break;
                    }
                }

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary));
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.events_toolbar);
        toolbar.setTitle(getString(R.string.events));
        setSupportActionBar(toolbar);
    }

    private void setupEventList(){
        recyclerView = (RecyclerView) findViewById(R.id.event_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        eventList = new LinkedList<>();
        Event[] someEvents = new Event[eventList.size()];
        eventAdapter = new EventAdapter(eventList.toArray(someEvents), this);
        recyclerView.setAdapter(eventAdapter);
    }

    private void setupNoEvents(){
        noEventsLayer = (RelativeLayout) findViewById(R.id.no_events_layer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_list, menu);
        //getMenuInflater().inflate(R.menu.debuging_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_add_new_event: {
                Intent intent = new Intent(this, CreateNewEventActivity.class);
                startActivityForResult(intent, EVENT_LIST_SCREEN_CODE);
                break;
            }
            case R.id.action_clear:{
                DatabaseTypeLoader.getInstance().getDatabase().clearAllEvents();
                break;
            }
            case R.id.add_new:{
                try{

                    Event event = new Event();
                    event.setIdentifier(1);
                    event.setHour(17); event.setMinute(9);
                    event.setRinger(AudioManager.RINGER_MODE_SILENT);
                    event.setName("Fake event like a boss.");

                    EventManager.getInstance().addEvent(event);

                    Event event2 = new Event();
                    event2.setIdentifier(12);
                    event2.setHour(9); event2.setMinute(00);
                    event2.setRinger(AudioManager.RINGER_MODE_VIBRATE);
                    event2.setName("YOLO meeting.");

                    EventManager.getInstance().addEvent(event2);

                    ToastUtil.showAppMsg("Created fake event!");

                }catch(EventAddException e){
                    e.printStackTrace();
                }
                break;
            }
            case R.id.action_contact_dev:{
                startEmailApp();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==EVENT_LIST_SCREEN_CODE && resultCode==RESULT_OK){

            if (data !=null && data.hasExtra(BundleExtras.Event_OBJECT.toString())){
                try{
                    Event newEvent = (Event) data.getExtras().get(BundleExtras.Event_OBJECT.toString());
                    EventManager.getInstance().addEvent(newEvent);
                }
                catch(EventAddException e){
                    e.printStackTrace();
                }
            }
        }
        finish();
        startActivity(getIntent());
    }

    private boolean isIntroBeenShown(){
        SharedPreferences settings = getSharedPreferences(Constants.SETTINGS_FILENAME, MODE_PRIVATE);
        return settings.getBoolean(BundleExtras.INTRO_SHOWN.toString(), true);
    }
    private void showIntro(){
        Intent intent = new Intent(EventListActivity.this, Introduction.class);
        startActivity(intent);
        finish();
    }
    private void startEmailApp(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", developerEmail, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.this_is_about, getString(R.string.app_name)+ BuildConfig.VERSION_NAME));
        startActivity(Intent.createChooser(intent, getString(R.string.email_dev)));
    }
}
