package com.iven.lfflfeedreader.mainact;

import android.content.Context;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

	public static Context contextOfApplication;

    String LFFLFEEDURL = "http://feeds.feedburner.com/lffl";
    RSSFeed lfflfeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contextOfApplication = getApplicationContext();

		if (Preferences.navTintEnabled(getBaseContext())) {
			getWindow().setNavigationBarColor(getResources().getColor(R.color.quantum_grey));
		}

		if (Preferences.englishEnabled(getBaseContext())) {
			String languageToLoad  = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
			getBaseContext().getResources().getDisplayMetrics());
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

	private void startLisActivity(RSSFeed lfflfeed) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("feed", lfflfeed);
		Intent i = new Intent(SplashActivity.this, ListActivity.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();

	}

	public static Context getContextOfApplication(){
		return contextOfApplication;
	}

	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			DOMParser Do = new DOMParser();
			lfflfeed = Do.parseXml(LFFLFEEDURL);

            return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			startLisActivity(lfflfeed);
        }

	}
}
