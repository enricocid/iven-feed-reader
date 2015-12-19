package com.iven.lfflfeedreader.mainact;

import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

public class SplashActivity extends AppCompatActivity {

    //the default feed
    public static String value = "http://feeds.feedburner.com/xdadevs";

    //the items
    RSSFeed lfflfeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //this is the method to handle navigation view feeds and custom feeds
        //here we receive data (feedselected) from list activity and start a new async task to load
        //the news feed
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
				value = extras.getString("feedselected");
            }

            //set the default feed if the value returns null
			if (value == null) value = "http://feeds.feedburner.com/xdadevs";
        }

        //set the navbar tint if the preference is enabled
        if (Build.VERSION.SDK_INT >= 21){
		if (Preferences.navTintEnabled(getBaseContext())) {
			getWindow().setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.quantum_grey));
		}
        }

        //set LightStatusBar
        if (Build.VERSION.SDK_INT >= 23) {
        if (Preferences.applyLightIcons(getBaseContext())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        // Detect if there's a connection issue or not
		ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // If there's a connection problem
		if (cM.getActiveNetworkInfo() == null) {

            // Show alert splash
			setContentView(R.layout.splash_no_internet);
            new Handler().postDelayed(new Runnable() {
                       public void run() {

                           // and finish the splash activity
                           SplashActivity.this.finish();

                       }
                   }, 2000);

		} else {

            //else :P, start the default splash screen and parse the RSSFeed and save the object
			setContentView(R.layout.splash);
			new AsyncLoadXMLFeed().execute();

		}
    }

    @Override
    public void onResume() {
        super.onResume();

            Bundle extras = getIntent().getExtras();

        //set the selected feed on resume the activity
        if(extras != null) {
            value = extras.getString("feedselected");
        }

        //set the default feed if the value returns null
        if (value == null) value = "http://feeds.feedburner.com/xdadevs";
        }


    //Using intents we send the lfflfeed (the parsed xml to populate the listview)
    // from the async task to listactivity
	private void startLisActivity(RSSFeed lfflfeed) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("feed", lfflfeed);
		Intent i = new Intent(SplashActivity.this, ListActivity.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();
                }

    //parse the xml in an async task
    //An asynchronous task is defined by a computation that runs on a background thread
    // and whose result is published on the UI thread.
	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			DOMParser Do = new DOMParser();
			lfflfeed = Do.parseXml(value);

            return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
            startLisActivity(lfflfeed);
        }

	}
}
