package com.adeebnqo.alarmapp.models;

/**
 * Created by adeeb on 5/24/15.
 */
public class NavigationItem {

    private int drawable;
    private String title;

    public NavigationItem(int drawable, String title){
        this.drawable = drawable;
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }
}
