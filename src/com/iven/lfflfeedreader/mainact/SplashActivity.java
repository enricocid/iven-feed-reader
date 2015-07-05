package com.iven.lfflfeedreader.mainact;


import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;

public class SplashActivity extends Activity {

	String LFFLFEEDURL = "http://feeds.feedburner.com/lffl/";
	RSSFeed lfflfeed;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cM.getActiveNetworkInfo() == null) {
			// Inflate any custom view
			
			Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.internet_alert, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            view.setBackgroundColor(Color.RED); 
            snack.show();	
				   new Handler().postDelayed(new Runnable() {
					   public void run() {
				   SplashActivity.this.finish();

					   }
			        }, 2000);

		} else {

			new AsyncLoadXMLFeed().execute();

		}

	}

	private void startLisActivity(RSSFeed lfflfeed) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("feed", lfflfeed);

		// launch List activity
		Intent i = new Intent(SplashActivity.this, ListActivity.class);
		i.putExtras(bundle);
		startActivity(i);
		finish();

	}

	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			// Obtain feed
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
