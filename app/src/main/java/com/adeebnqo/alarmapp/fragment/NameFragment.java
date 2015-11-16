package com.adeebnqo.alarmapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.android.alarmclock.Alarm;

public class NameFragment extends Fragment{

    private Alarm currentEvent;

    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private ToggleButton sunday;

    private EditText editText;
    private ImageButton nextButton;
    private ImageButton prevButton;

    private View.OnClickListener nextButtonClickListener;
    private OnNameGivenListener mListener;
    private String eventName;

    public static NameFragment newInstance(Alarm someEvent) {
        NameFragment fragment = new NameFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleExtras.Event_OBJECT.toString(), someEvent);
        fragment.setArguments(args);
        return fragment;
    }

    public NameFragment() {
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
        return inflater.inflate(R.layout.fragment_name_and_repeats, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNameGivenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNameGivenListener");
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

        editText = (EditText) view.findViewById(R.id.editText);
        prevButton = (ImageButton) view.findViewById(R.id.prev_button);
        nextButton = (ImageButton) view.findViewById(R.id.next_button);

        prevButton.setVisibility(View.GONE);

        if (currentEvent!=null) {
            editText.setText(currentEvent.label);
        }

        if (nextButtonClickListener == null){
            nextButtonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventName = editText.getText().toString();
                    if (TextUtils.isEmpty(eventName)){
                        editText.setError(getString(R.string.empty_event_name));
                    }else{
                        if (mListener!=null){
                            mListener.onGetName(eventName);
                        }
                    }
                }
            };
        }
        nextButton.setOnClickListener(nextButtonClickListener);
    }

    public interface OnNameGivenListener{
        void onGetName(String eventName);
    }
}
