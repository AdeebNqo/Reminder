package com.adeebnqo.alarmapp.models;

import android.content.Context;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.adeebnqo.alarmapp.utils.ApplicationData;

/**
 * Created by adeeb on 3/8/15.
 */
@DatabaseTable(tableName = "events")
public class Event implements Serializable{

    @DatabaseField
    private String name;
    @DatabaseField
    private int minute;
    @DatabaseField
    private int hour;
    @DatabaseField
    private int ringer;
    @DatabaseField
    private int duration;
    @DatabaseField
    private boolean active = true;
    @DatabaseField(generatedId = true)
    private int identifier = -1;
    @ForeignCollectionField
    private Collection<Day> days = new TreeSet<>();


    public Event(){

    }
    public String getName(){
        return name;
    }
    public void setName(String someName){
        name = someName;
    }

    public int getRinger() {
        return ringer;
    }
    public void setRinger(int ringer) {
        this.ringer = ringer;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<Integer> getDays() {
        Set<Integer> newSet = new TreeSet<>();
        for (Day day : days){
            newSet.add(day.getCalendarKey());
        }
        return newSet;
    }
    public void setDays(Set<Integer> days) {
        Set<Day> newSet = new TreeSet<>();
        for (Integer day : days){
            Day someDay = new Day();
            someDay.setCalendarKey(day);
            someDay.setEvent(this);
            newSet.add(someDay);
        }
        this.days = newSet;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier){
        this.identifier = identifier;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }
    public int getMinute(){
        return minute;
    }

    public void setHour(int hour){
        this.hour = hour;
    }
    public int getHour(){
     return hour;
    }

    public Date getRawTime(){

        Date d = getRawTimeAsCalender().getTime();

        return d;
    }

    public Calendar getRawTimeAsCalender(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());

        return cal;
    }

    public String getFormattedTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Date d = getRawTime();

        return sdf.format(d);
    }
    public String getFormattedDuration(){
        //TODO implement this
        return "30 min";
    }

    public String toString(){

        Context context = ApplicationData.getContext();

        String rep = String.format("%1$s : "+identifier+", %2$s : "+duration+", %3$s : "+active+", %4$s : "+ringer+" %5$s: ",
                "identifier",
                        "duration",
                        "active",
                        "ringer",
                        "days");
        for (int day : getDays()){
            switch(day) {
                case Calendar.MONDAY: {
                    rep += "Monday";
                    break;
                }
                case Calendar.TUESDAY: {
                    rep += "Tuesday";
                    break;
                }
                case Calendar.WEDNESDAY: {
                    rep += "Wednesday";
                    break;
                }
                case Calendar.THURSDAY: {
                    rep += "Thursday";
                    break;
                }
                case Calendar.FRIDAY: {
                    rep += "Friday";
                    break;
                }
                case Calendar.SATURDAY: {
                    rep += "Saturday";
                    break;
                }
                case Calendar.SUNDAY: {
                    rep += "Sunday";
                    break;
                }
            }
        }
        return rep;
    }

    //the following method should not used be used in the top
    //layers
    public Collection<Day> getInnerDays(){
        return days;
    }

}
