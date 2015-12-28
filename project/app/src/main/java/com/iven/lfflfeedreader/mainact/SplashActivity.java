package com.iven.lfflfeedreader.mainact;

import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

public class SplashActivity extends AppCompatActivity {

    //the default feed
    public static String default_feed_value = "http://feeds.feedburner.com/xdadevs";

    //the items
    RSSFeed lfflfeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //set the navbar tint if the preference is enabled
		Preferences.applyNavTint(this, getBaseContext());

		//set LightStatusBar
		Preferences.applyLightIcons(this);

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

    //Using intents we send the lfflfeed (the parsed xml to populate the listview)
    // from the async task to listactivity
	private void startListActivity(RSSFeed lfflfeed) {

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
			lfflfeed = Do.parseXml(default_feed_value);

            return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
            startListActivity(lfflfeed);
        }

	}
}
