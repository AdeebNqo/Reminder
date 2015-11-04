package com.adeebnqo.alarmapp.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by adeeb on 4/27/15.
 */
public class Convertor {
    public static Set<String> getStringSet(Set<Integer> values){
        Set<String> set = new TreeSet<String>();

        if (values!=null){
            Iterator<Integer> iterator = values.iterator();
            while(iterator.hasNext()){
                Integer currentValue = iterator.next();
                set.add(String.valueOf(currentValue));
            }
        }
        return set;
    }
    public static Set<Integer> getIntegerSet(Set<String> values){
        Set<Integer> arrayList = new TreeSet<>();

        if (values!=null){
            Iterator<String> iterator = values.iterator();
            while (iterator.hasNext()) {
                String currentValue = iterator.next();
                arrayList.add(Integer.valueOf(currentValue));
            }
        }
        return arrayList;
    }

    public static float getPxFromDp(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        float px = dp * density;
        return px;
    }
}
