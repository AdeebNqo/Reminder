package com.adeebnqo.alarmapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.android.alarmclock.Alarm;

public class TimeFragment extends Fragment {

    private Alarm currentEvent;

    private ImageButton prevButton;
    private ImageButton nextButton;
    private TimePicker timePicker;

    private onTimeSelectListener mListener;
    private View.OnClickListener prevClickListener;
    private View.OnClickListener nextClickListener;

    public static TimeFragment newInstance(Alarm someEvent) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleExtras.Event_OBJECT.toString(), someEvent);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentEvent = getArguments().getParcelable(BundleExtras.Event_OBJECT.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (onTimeSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onTimeSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        timePicker = (TimePicker) view.findViewById(R.id.event_time_picker);
        prevButton = (ImageButton) view.findViewById(R.id.prev_button);
        nextButton = (ImageButton) view.findViewById(R.id.next_button);

        if (nextClickListener == null){
            nextClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.onTimeSelected(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                }
            };
        }
        if (prevClickListener == null){
            prevClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.backPressedOnTime();
                    }
                }
            };
        }

        prevButton.setOnClickListener(prevClickListener);
        nextButton.setOnClickListener(nextClickListener);
        timePicker.setIs24HourView(true);

        if (currentEvent!=null){
            timePicker.setCurrentHour(currentEvent.hour);
            timePicker.setCurrentMinute(currentEvent.minutes);
        }
    }

    public interface onTimeSelectListener{
        void backPressedOnTime();
        void onTimeSelected(int hour, int minute);
    }


}
