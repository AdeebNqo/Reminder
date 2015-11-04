package com.adeebnqo.alarmapp.models;

/**
 * Created by adeeb on 7/4/15.
 */
public class DataAvailableEvent {
    private boolean dataAvailable;

    public DataAvailableEvent(boolean available){
        dataAvailable = available;
    }
    public boolean isDataAvailable() {
        return dataAvailable;
    }
}
