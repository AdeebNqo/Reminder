package com.adeebnqo.alarmapp.database.sharedpreference;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.utils.ApplicationData;

/**
 * Created on 8/10/15.
 */
public class SharedPreferencesDataStoreV2 implements DataProvider {
    private String cacheFilename = getClass().getName()+"-CACHE";

    SharedPreferences cache;
    Gson jsonBuilder;
    Random randomNumGenerator;

    public SharedPreferencesDataStoreV2() {

        cache = ApplicationData.getContext().getSharedPreferences(cacheFilename, 0);
        jsonBuilder = new Gson();
        randomNumGenerator = new Random();

    }

    @Override
    public boolean hasStoredEvents() {
        boolean hasValues = cache.getAll().size() > 0;
        return hasValues;
    }

    @Override
    public List<Event> getStoredEvents() {
        List<Event> events = new ArrayList<>();

        Map<String, ?> itemMap = cache.getAll();
        for (String key : itemMap.keySet()){
            try {
                int identifier = Integer.parseInt(key);
                events.add(getEvent(identifier));
            }catch (Exception e){
                Log.d(getClass().getName(), "getStoredEvents(). Cannot retrieve event with id="+key);
            }
        }

        return events;
    }

    @Override
    public void clearAllEvents() {
        cache.edit().clear().commit();
    }

    @Override
    public void addEvent(Event value) throws EventAddException {
        int identifier = Math.abs(randomNumGenerator.nextInt());
        if (value.getIdentifier() == -1) {
            value.setIdentifier(identifier);
        }
        String serializedEvent = jsonBuilder.toJson(value);
        String eventId = String.valueOf(value.getIdentifier());
        cache.edit().putString(eventId, serializedEvent).commit();
    }

    @Override
    public void removeEvent(int identifier) {
        String eventId = String.valueOf(identifier);
        cache.edit().remove(eventId).commit();
    }

    @Override
    public void deactivateEvent(int identifier) {
        try {
            Event event = getEvent(identifier);
            event.setActive(false);
            removeEvent(identifier);
            addEvent(event);
        }catch (Exception e) {
            Log.d(getClass().getName(), "Cannot deactivate with id="+identifier+" since it does not exist.");
        }
    }

    @Override
    public void activateEvent(int identifier) {
        try {
            Event event = getEvent(identifier);
            event.setActive(true);
            removeEvent(identifier);
            addEvent(event);
        }catch (Exception e){
            Log.d(getClass().getName(), "Cannot activate with id="+identifier+" since it does not exist.");
        }
    }

    @Override
    public Event getEvent(int identifier) throws EventNotFoundException {
        String eventId = String.valueOf(identifier);
        String serializedEvent = cache.getString(eventId, "");
        if (TextUtils.isEmpty(serializedEvent)){
            throw new EventNotFoundException("Cannot find event with id="+eventId);
        }

        Event actualEvent = jsonBuilder.fromJson(serializedEvent, Event.class);
        return actualEvent;
    }

    @Override
    public void saveEverything() {
        //do not need to do anything
    }
}
