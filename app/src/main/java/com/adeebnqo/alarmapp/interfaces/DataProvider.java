package com.adeebnqo.alarmapp.interfaces;

import java.util.List;

import com.adeebnqo.alarmapp.exceptions.EventAddException;
import com.adeebnqo.alarmapp.exceptions.EventNotFoundException;
import com.adeebnqo.alarmapp.models.Event;

/**
 * Created by adeeb on 3/8/15.
 */
public interface DataProvider {
    boolean hasStoredEvents();
    List<Event> getStoredEvents();
    void clearAllEvents();
    void addEvent(Event value) throws EventAddException;
    void removeEvent(int identifier);
    void deactivateEvent(int identifier);
    void activateEvent(int identifier);
    Event getEvent(int identifier) throws EventNotFoundException;

    void saveEverything();
}
