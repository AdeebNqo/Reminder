package com.adeebnqo.alarmapp.fragment.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeebnqo.alarmapp.R;

/**
 * Created by adeeb on 7/2/15.
 */
public class FirstSlide extends Fragment {

    private TextView expandedText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.firstslide, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        expandedText = (TextView) getView().findViewById(R.id.textView);
        expandedText.setText(getString(R.string.meetings_expanded, getString(R.string.app_name)));
    }
}
