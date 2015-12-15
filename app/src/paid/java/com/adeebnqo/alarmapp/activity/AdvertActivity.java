package com.adeebnqo.alarmapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.adeebnqo.alarmapp.R;

public class AdvertActivity extends Activity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar();
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
