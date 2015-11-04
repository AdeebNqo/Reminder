package com.adeebnqo.alarmapp.database.ormlite;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Collection;
import java.util.List;

import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.models.Day;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.utils.ApplicationData;

/**
 * Created by adeeb on 5/20/15.
 */
public class OrmLiteDatabase implements DataProvider {


    private DatabaseHelper helper;

    public OrmLiteDatabase(){
        helper = new DatabaseHelper(ApplicationData.getContext());
    }

    @Override
    public boolean hasStoredEvents() {
        return !helper.getEventRuntimeDao().queryForAll().isEmpty();
    }

    @Override
    public List<Event> getStoredEvents() {
        List<Event> events = helper.getEventRuntimeDao().queryForAll();
        return events;
    }

    @Override
    public void clearAllEvents() {
        helper.getEventRuntimeDao().delete(helper.getEventRuntimeDao().queryForAll());
    }

    @Override
    public void addEvent(Event value) throws EventAddException {
        helper.getEventRuntimeDao().createIfNotExists(value);

        for (Day day : value.getInnerDays()){
            if( day.getEvent()==null){
                day.setEvent(value);
            }
            helper.getDayRuntimeDao().createIfNotExists(day);
        }
    }

    @Override
    public void removeEvent(int identifier) {
        Event toDeleteEvent = helper.getEventRuntimeDao().queryForId(identifier);

        helper.getDayRuntimeDao().delete(toDeleteEvent.getInnerDays());

        helper.getEventRuntimeDao().delete(toDeleteEvent);
    }

    @Override
    public void deactivateEvent(int identifier) {
        Event eventToUpdate = helper.getEventRuntimeDao().queryForId(identifier);
        eventToUpdate.setActive(false);
        helper.getEventRuntimeDao().update(eventToUpdate);
    }

    @Override
    public void activateEvent(int identifier) {
        Event eventToUpdate = helper.getEventRuntimeDao().queryForId(identifier);
        eventToUpdate.setActive(true);
        helper.getEventRuntimeDao().update(eventToUpdate);
    }

    @Override
    public Event getEvent(int identifier) throws EventNotFoundException {
        return helper.getEventRuntimeDao().queryForId(identifier);
    }

    @Override
    public void saveEverything() {
        helper.close();
    }
}
