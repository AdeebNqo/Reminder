package com.adeebnqo.alarmapp.fragment;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.models.BundleExtras;
import com.adeebnqo.alarmapp.utils.ToastUtil;
import com.android.alarmclock.Alarm;

public class DurationFragment extends Fragment {
    private OnDurationEnteredListener mListener;

    private ImageButton nextButton;
    private ImageButton prevButton;
    private EditText editText;
    private RadioGroup radioGroup;

    private View.OnClickListener nextClickListener;
    private View.OnClickListener prevClickListener;

    private Alarm currentEvent;

    private RadioButton muteButton;
    private RadioButton vibrateButton;
    private RadioButton normalButton;

    public DurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments() != null) {
                currentEvent = getArguments().getParcelable(BundleExtras.Event_OBJECT.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_duration, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDurationEnteredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDurationEnteredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        editText = (EditText) view.findViewById(R.id.editText2);
        prevButton = (ImageButton) view.findViewById(R.id.prev_button);
        nextButton = (ImageButton) view.findViewById(R.id.next_button);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        if (prevClickListener == null){
            prevClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.onBackPressedInDurationScreen();
                    }
                }
            };
        }
        if (nextClickListener == null){
            nextClickListener = new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        String durationVal = editText.getText().toString();

                        if (TextUtils.isEmpty(durationVal)) {
                            editText.setError(getString(R.string.empty_event_duration));
                        }else {

                            try {
                                int duration = Integer.parseInt(durationVal);

                                if (duration < 1) {
                                    editText.setError(getString(R.string.duration_cannot_be_zero));
                                } else {
                                    int ringerMode = 0;
                                    switch (radioGroup.getCheckedRadioButtonId()) {
                                        case R.id.mute_choice:
                                            ringerMode = AudioManager.RINGER_MODE_SILENT;
                                            break;
                                        case R.id.normal_choice:
                                            ringerMode = AudioManager.RINGER_MODE_NORMAL;
                                            break;
                                        case R.id.vibrate_choice:
                                            ringerMode = AudioManager.RINGER_MODE_VIBRATE;
                                            break;
                                    }
                                    mListener.onDurationProvided(ringerMode, duration);
                                }
                            } catch (Exception e) {
                                editText.setError(getString(R.string.invalid_duration));
                                e.printStackTrace();
                            }

                        }
                    }
                }
            };
        }

        nextButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
        nextButton.setOnClickListener(nextClickListener);
        prevButton.setOnClickListener(prevClickListener);

        if (currentEvent!=null) {
            switch (currentEvent.ringerMode){
                case AudioManager.RINGER_MODE_VIBRATE:
                    vibrateButton = (RadioButton) view.findViewById(R.id.vibrate_choice);
                    vibrateButton.setSelected(true);
                    break;
                case AudioManager.RINGER_MODE_SILENT:{
                    muteButton = (RadioButton) view.findViewById(R.id.mute_choice);
                    muteButton.setSelected(true);
                    break;
                }
                case AudioManager.RINGER_MODE_NORMAL:{
                    normalButton = (RadioButton) view.findViewById(R.id.normal_choice);
                    normalButton.setSelected(true);
                    break;
                }
            }

            int currentDuration = currentEvent.duration;
            if (currentDuration > 0){
                editText.setText(""+currentDuration);
            }
        }
    }

    public interface OnDurationEnteredListener {
        void onBackPressedInDurationScreen();
        void onDurationProvided(int ringerMode, int duration);
    }

}
