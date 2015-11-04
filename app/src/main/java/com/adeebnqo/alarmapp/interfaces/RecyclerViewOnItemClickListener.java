package com.adeebnqo.alarmapp.interfaces;

import android.view.View;

/**
 * Created by adeeb on 5/8/15.
 */
public interface RecyclerViewOnItemClickListener{
    void onClicked(int position);
    void onSubViewClicked(View view, int position);
}
