package com.adeebnqo.alarmapp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by adeeb on 5/20/15.
 */
@DatabaseTable(tableName = "days")
public class Day implements Serializable, Comparable{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Integer calendarKey;

    @DatabaseField(foreign = true,  foreignAutoRefresh = true, canBeNull = false)
    private transient Event someEvent;

    public Integer getCalendarKey(){
        return calendarKey;
    }
    public void setCalendarKey(int someValue){
        calendarKey = someValue;
    }

    public void setEvent(Event someEvent) {
        this.someEvent = someEvent;
    }
    public Event getEvent(){
        return someEvent;
    }

    @Override
    public int compareTo(Object o) {
        Day day = (Day) o;
        if (calendarKey == day.getCalendarKey()){
            return 0;
        }
        return calendarKey > day.getCalendarKey() ? 1 : -1;
    }
}
