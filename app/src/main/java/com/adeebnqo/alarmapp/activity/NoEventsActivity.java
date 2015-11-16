package com.adeebnqo.alarmapp.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.adapters.NavAdapter;
import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.models.NavigationItem;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import com.android.alarmclock.Alarm;

public class NoEventsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{


    private static int NO_EVENTS_SCREEN_CODE = 1;

    private Toolbar toolbar;
    private TextView noEventsText;


    private DrawerLayout drawerLayout;
    private ListView leftDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private List<NavigationItem> navItems = new ArrayList<>();

    private String aboutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_events2);

        aboutTitle = getString(R.string.title_activity_advert);

        setupToolbar();
        setupNoEventsText();
        setupNavDrawer();
    }

    private void setupNavDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        leftDrawer = (ListView) findViewById(R.id.left_drawer);

        navItems.add(new NavigationItem(R.drawable.information_outline, aboutTitle));
        NavAdapter adapter = new NavAdapter(NoEventsActivity.this, R.id.left_drawer, navItems);
        leftDrawer.setAdapter(adapter);
        leftDrawer.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this ,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary));
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(leftDrawer);
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.no_events_tooolbar);
        toolbar.setTitle(getString(R.string.no_events));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
    }
    private void setupNoEventsText(){
        noEventsText = (TextView) findViewById(R.id.no_events_main_text);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(leftDrawer)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_list, menu);
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
                startActivityForResult(intent, NO_EVENTS_SCREEN_CODE);
                break;
            }
            case R.id.action_clear:{
                Intent intent = new Intent(this, AdvertActivity.class);
                startActivity(intent);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
        if (navItems.get(position).getTitle().equalsIgnoreCase(aboutTitle)){
            drawerLayout.closeDrawer(leftDrawer);
            Intent intent = new Intent(this, AdvertActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==NO_EVENTS_SCREEN_CODE && resultCode==RESULT_OK){
            Alarm newEvent = data.getExtras().getParcelable(BundleExtras.Event_OBJECT.toString());

            CustomAlarms.addAlarm(newEvent);
            Intent intent = new Intent(NoEventsActivity.this, LandingActivityLoader.class);
            startActivity(intent);
            finish();
        }
    }
}
