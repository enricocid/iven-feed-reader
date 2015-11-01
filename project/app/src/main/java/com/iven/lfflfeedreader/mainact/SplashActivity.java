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

    public static String value = "http://feeds.feedburner.com/d0od";
    RSSFeed lfflfeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
				value = extras.getString("feedselected");
            }
			if (value == null) value = "http://feeds.feedburner.com/d0od";
        }

		if (Preferences.navTintEnabled(getBaseContext())) {
			getWindow().setNavigationBarColor(getResources().getColor(R.color.quantum_grey));
		}

		ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cM.getActiveNetworkInfo() == null) {
			setContentView(R.layout.splash_no_internet);
            new Handler().postDelayed(new Runnable() {
                       public void run() {
                           SplashActivity.this.finish();

                       }
                   }, 2000);

		} else {
			setContentView(R.layout.splash);
			new AsyncLoadXMLFeed().execute();

		}
    }

    @Override
    public void onResume() {
        super.onResume();

            Bundle extras = getIntent().getExtras();
        if(extras != null) {
            value = extras.getString("feedselected");
        }
        if (value == null) value = "http://feeds.feedburner.com/d0od";
        }

	private void startLisActivity(RSSFeed lfflfeed) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("feed", lfflfeed);
		Intent i = new Intent(SplashActivity.this, ListActivity.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();
                }

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
