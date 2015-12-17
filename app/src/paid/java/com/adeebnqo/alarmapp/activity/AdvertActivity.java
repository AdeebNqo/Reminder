package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.adeebnqo.alarmapp.BuildConfig;
import com.adeebnqo.alarmapp.R;

public class AdvertActivity extends Activity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar();

        TextView aboutText = (TextView) findViewById(R.id.textView5);
        aboutText.setText(getString(R.string.about, BuildConfig.VERSION_NAME, BuildConfig.CodeName));
    }

    private void setupToolbar(){
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
}
