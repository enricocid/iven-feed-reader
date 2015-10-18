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

public class SplashActivity extends AppCompatActivity {


    String LFFLFEEDURL = "http://feeds.feedburner.com/lffl";
	RSSFeed lfflfeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
