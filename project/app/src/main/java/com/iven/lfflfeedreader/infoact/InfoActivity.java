package com.iven.lfflfeedreader.infoact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.utils.Preferences;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		//apply activity's theme if dark theme is enabled
		Preferences.applyTheme(this);

		super.onCreate(savedInstanceState);

        //set the layout
		setContentView(R.layout.activity_settings);

        //set the navbar tint if the preference is enabled
        if (Build.VERSION.SDK_INT >= 21){
            if (Preferences.navTintEnabled(getBaseContext())) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.primary));
            }
        }

        //set LightStatusBar
        if (Build.VERSION.SDK_INT >= 23) {
            if (Preferences.applyLightIcons(getBaseContext())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        //set the toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

        //provide back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, 0);
            }
        });

        //set toolbar title
        getSupportActionBar().setTitle(R.string.settings);
    }

}
