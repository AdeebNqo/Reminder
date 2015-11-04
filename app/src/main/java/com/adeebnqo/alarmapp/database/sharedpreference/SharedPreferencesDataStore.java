package com.adeebnqo.alarmapp.database.sharedpreference;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.adeebnqo.alarmapp.utils.Constants;
import com.adeebnqo.alarmapp.utils.Convertor;

/**
 * Created by adeeb on 3/8/15.
 */
@Deprecated
public class SharedPreferencesDataStore implements DataProvider{


    private String cacheFilename = "CACHE";

    private HashMap<Integer, SharedPreferences> map;

    public SharedPreferencesDataStore(){

        map = new HashMap<Integer, SharedPreferences>();
        SharedPreferences cache = ApplicationData.getContext().getSharedPreferences(cacheFilename, 0);
        int numKeys = cache.getInt("numKeys", -1);
        if (numKeys!= -1) {
            for (int i=0; i<numKeys; ++i) {
                int key = cache.getInt("key"+i, -1);
                map.put(key, ApplicationData.getContext().getSharedPreferences(key+"",0));
            }
        }
    }

    public boolean hasStoredEvents(){
        return map.keySet().size()!=0;
    }

    public List<Event> getStoredEvents(){

        List<Event> events = new LinkedList<Event>();

        Set<Integer> keys = map.keySet();
        Iterator<Integer> iterator = keys.iterator();
        while(iterator.hasNext()){
            try {
                Event event = getEvent(iterator.next());
                events.add(event);
            }catch(EventNotFoundException e){
                    e.printStackTrace();
            }
        }
        return events;
    }

    public void clearAllEvents(){
        Set<Integer> keys = map.keySet();
        Iterator<Integer> iterator = keys.iterator();
        while(iterator.hasNext()){
            Integer currentKey = iterator.next();
            SharedPreferences sharedPreferences = map.get(currentKey);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
        map.clear();
    }

    public void addEvent(Event value) throws EventAddException{
        try {
            Integer identifier = value.getIdentifier();

            SharedPreferences settings = ApplicationData.getContext().getSharedPreferences(identifier+"", 0);
            SharedPreferences.Editor prefsEditor = settings.edit();

            prefsEditor.putString("name", value.getName());
            prefsEditor.putInt("hour", value.getHour());
            prefsEditor.putInt("minute", value.getMinute());
            prefsEditor.putInt("ringer", value.getRinger());
            prefsEditor.putFloat("duration", (float) value.getDuration());
            prefsEditor.putBoolean("active", value.isActive());
            prefsEditor.putStringSet("days", Convertor.getStringSet(value.getDays()));

            prefsEditor.commit();
            map.put(identifier, settings);
        }catch(Exception e){
            Log.d(Constants.LOG_TAG, "Event (id=" + value.getIdentifier() + ") cannot be added.");
            e.printStackTrace();
            throw new EventAddException("Event (id=" + value.getIdentifier() + ") cannot be added.");
        }
    }

    @Override
    public void removeEvent(int identifier) {
        map.remove(Integer.valueOf(identifier));
    }


    @Override
    public void deactivateEvent(int identifier) {
        SharedPreferences preferences = map.get(Integer.valueOf(identifier));

        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putBoolean("active",false);
        prefsEditor.commit();
    }
    @Override
    public void activateEvent(int identifier){
        SharedPreferences preferences = map.get(Integer.valueOf(identifier));

        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putBoolean("active",true);
        prefsEditor.commit();
    }

    @Override
    public void saveEverything(){
        SharedPreferences cache = ApplicationData.getContext().getSharedPreferences(cacheFilename, 0);
        SharedPreferences.Editor prefsEditor = cache.edit();

        Set<Integer> keys = map.keySet();
        int index = 0;
        for (Integer key: keys){
            prefsEditor.putInt("key"+index, key);
            ++index;
        }
        prefsEditor.putInt("numKeys", index);
    }

    @Override
    public Event getEvent(int identifier) throws EventNotFoundException {
        SharedPreferences eventData = map.get(identifier);

        Event event = null;
        if (eventData!=null) {
            event = new Event();
            event.setName(eventData.getString("name", "default"));
            event.setHour(eventData.getInt("hour", -1));
            event.setMinute(eventData.getInt("minute", -1));
            event.setRinger(eventData.getInt("ringer", -1));
            event.setDuration(eventData.getInt("duration", -1));
            event.setActive(eventData.getBoolean("active", false));
            event.setDays(Convertor.getIntegerSet(eventData.getStringSet("days", null)));
            event.setIdentifier(identifier);
        }else{
            throw new EventNotFoundException("Event ID: "+identifier+" not found in database.");
        }
        return event;
    }
}
