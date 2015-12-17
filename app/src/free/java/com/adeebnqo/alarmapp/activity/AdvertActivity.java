package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;

import com.adeebnqo.alarmapp.utils.AnalyticsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.R;

public class AdvertActivity extends Activity {

    private Toolbar toolbar;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar();
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS){
            setupAd();
        }

        AnalyticsManager.getInstance().sendEvent("Views", "About screen");

        TextView aboutText = (TextView) findViewById(R.id.textView5);
        aboutText.setText(getString(R.string.about, BuildConfig.VERSION_NAME, BuildConfig.CodeName));
    }

    private void setupToolbar() {
        if (toolbar == null){
            toolbar = (Toolbar) findViewById(R.id.advert_tooolbar);
            toolbar.setTitle(getString(R.string.title_activity_advert));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdvertActivity.this.onBackPressed();
                }
            });
        }
    }

    private void setupAd() {
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest;
        if (BuildConfig.IS_DEBUG) {
            adRequest = new AdRequest.Builder().addTestDevice("9D5C351F348903FC33FF54FC1C2DFF61").build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        adView.loadAd(adRequest);
    }
}
