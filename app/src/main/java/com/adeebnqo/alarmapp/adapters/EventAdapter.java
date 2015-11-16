package com.adeebnqo.alarmapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.activity.EventActivity;
import com.adeebnqo.alarmapp.activity.EventListActivity;
import com.adeebnqo.alarmapp.interfaces.RecyclerViewOnItemClickListener;
import com.adeebnqo.alarmapp.loaders.CustomAlarms;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.ApplicationData;
import com.android.alarmclock.Alarm;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements RecyclerViewOnItemClickListener{

    private Context context;
    private Alarm[] events;

    static Random random = new Random();
    static int[] iconColors = ApplicationData.getContext().getResources().getIntArray(R.array.icon_colors);

    public EventAdapter(Alarm[] myDataset, Activity activity) {
        events = myDataset;
        context = activity;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView eventName;
        TextView eventTime;
        ImageView ringerType;
        CheckBox activeCheckbox;
        View divider;

        GradientDrawable iconDrawable;


        RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

        public ViewHolder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.event_name);
            eventTime = (TextView) v.findViewById(R.id.event_time);
            ringerType = (ImageView) v.findViewById(R.id.event_type_icon);
            activeCheckbox = (CheckBox) v.findViewById(R.id.active_checkBox);
            divider = v.findViewById(R.id.divider);

            iconDrawable = (GradientDrawable) ringerType.getBackground();

            //use random color for icon
            int color = iconColors[random.nextInt(iconColors.length-1)];
            iconDrawable.setColor(color);
            iconDrawable.setStroke(23, color);

            activeCheckbox.setOnClickListener(ViewHolder.this);
        }
        public void setOnItemClickListener(RecyclerViewOnItemClickListener someRecyclerViewOnItemClickListener){
            recyclerViewOnItemClickListener = someRecyclerViewOnItemClickListener;
        }

        @Override
        public void onClick(View view) {
            int eventPos = getAdapterPosition();
            if (activeCheckbox.getId()==view.getId()){
                recyclerViewOnItemClickListener.onSubViewClicked((CheckBox)view, eventPos);
            }else {
                recyclerViewOnItemClickListener.onClicked(eventPos);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.event, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        vh.setOnItemClickListener(this);
        v.setOnClickListener(vh);
        return vh;
    }

    // Replacing the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Alarm chosenEvent = events[position];

        //setting time and name of event
        holder.eventTime.setText(chosenEvent.getFormattedTime());
        holder.eventName.setText(chosenEvent.label);

        //setting (in)active using checkbox on the side
        if (chosenEvent.enabled){
            holder.activeCheckbox.setChecked(true);
        }else{
            holder.activeCheckbox.setChecked(false);
        }

        //changing ringer icon
        switch (chosenEvent.ringerMode){
            case AudioManager.RINGER_MODE_NORMAL:{
                holder.ringerType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_normal_ring));
                break;
            }
            case AudioManager.RINGER_MODE_SILENT:{
                holder.ringerType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_silent));
                break;
            }
            case AudioManager.RINGER_MODE_VIBRATE:{
                holder.ringerType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vibrate));
                break;
            }
            default:{
                holder.ringerType.setBackgroundColor(context.getResources().getColor(android.R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.length;
    }

    //go to the event detail activity
    @Override
    public void onClicked(int position) {
        Alarm chosenEvent = events[position];

        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(BundleExtras.Event_ID.toString(), chosenEvent.id);

        ((Activity) context).startActivityForResult(intent, EventListActivity.EVENT_LIST_SCREEN_CODE);
    }

    //(de)activating event
    @Override
    public void onSubViewClicked(View view, int position) {
        if (view instanceof CheckBox){
            CheckBox box = (CheckBox) view;
            Alarm chosenEvent = events[position];
            if (box.isChecked()){
                CustomAlarms.activateAlarm(chosenEvent);
            }else{
                CustomAlarms.deactivateAlarm(chosenEvent);
            }
        }
    }

    public void changeEventList(Alarm[] newEvents){
        events = newEvents;
        notifyDataSetChanged();
    }
}
