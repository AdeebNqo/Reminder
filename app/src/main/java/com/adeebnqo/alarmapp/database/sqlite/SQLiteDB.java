package com.adeebnqo.alarmapp.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.models.Event;

/**
 * Created by adeeb on 3/8/15.
 */
public class SQLiteDB implements DataProvider{

    public boolean hasStoredEvents(){
        return false;
    }

    @Override
    public List<Event> getStoredEvents() {
        return null;
    }

    @Override
    public void removeEvent(int identifier) {

    }

    @Override
    public void deactivateEvent(int identifier) {

    }

    @Override
    public void activateEvent(int identifier) {

    }

    @Override
    public Event getEvent(int identifier) throws EventNotFoundException {
        return null;
    }

    @Override
    public void saveEverything() {

    }

    public void clearAllEvents(){

    }
    public void addEvent(Event value){

    }
}
