package com.iven.lfflfeedreader.infoact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.utils.Preferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		//apply activity's theme if dark theme is enabled
		Preferences.applyTheme3(this);

		super.onCreate(savedInstanceState);

        //set the layout
		setContentView(R.layout.activity_settings);

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

    }

}
