package com.adeebnqo.alarmapp.managers;

import android.util.Log;

import java.util.Set;

import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.exceptions.EventUpdateException;
import com.adeebnqo.alarmapp.loaders.DatabaseTypeLoader;
import com.adeebnqo.alarmapp.models.Event;
import com.adeebnqo.alarmapp.scheduling.Scheduler;
import com.adeebnqo.alarmapp.utils.Constants;

/**
 * Created by adeeb on 4/28/15.
 */
public class EventManager {

    private static EventManager INSTANCE;
    private EventManager(){}
    public static EventManager getInstance(){
        if (INSTANCE==null) {
            INSTANCE = new EventManager();
        }
        return INSTANCE;
    }

    public void addEvent(Event event) throws EventAddException {
        DatabaseTypeLoader.getInstance().getDatabase().addEvent(event);
        Scheduler.getInstance().schedule(event);
    }
    public void removeEvent(Event event) {
        DatabaseTypeLoader.getInstance().getDatabase().removeEvent(event.getIdentifier());
        Scheduler.getInstance().unSchedule(event);
    }

    public void activateEvent(Event event){
        event.setActive(true);
        DatabaseTypeLoader.getInstance().getDatabase().activateEvent(event.getIdentifier());
        Scheduler.getInstance().schedule(event);
    }
    public void deActivateEvent(Event event){
        event.setActive(false);
        DatabaseTypeLoader.getInstance().getDatabase().deactivateEvent(event.getIdentifier());
        Scheduler.getInstance().unSchedule(event);
    }

    public Event getEvent(Integer identifier) throws EventNotFoundException {
        Event event = DatabaseTypeLoader.getInstance().getDatabase().getEvent(identifier);
        return event;
    }

    public void updateEvent(Event event) throws EventUpdateException {
        //TODO
    }

    public void updateEventDays(Event event, Set<Integer> newDays) throws EventUpdateException{
        try {
            DatabaseTypeLoader.getInstance().getDatabase().removeEvent(event.getIdentifier());
            event.setDays(newDays);
            DatabaseTypeLoader.getInstance().getDatabase().addEvent(event);
        }catch (Exception e) {
            throw new EventUpdateException("Event (id="+event.getIdentifier()+") cannot be updated");
        }
    }
}
