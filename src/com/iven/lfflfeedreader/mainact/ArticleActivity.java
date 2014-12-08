package com.iven.lfflfeedreader.mainact;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;

@SuppressLint("InlinedApi")
public class ArticleActivity extends FragmentActivity {

	RSSFeed feed;
	int pos;
	private DescAdapter adapter;
	private ViewPager pager;
	boolean hideSystemUi = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the feed object and the position from the Intent
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		pos = getIntent().getExtras().getInt("pos");

		// Initialize the views
		adapter = new DescAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);

		// Set Adapter to pager:
		pager.setAdapter(adapter);
		pager.setCurrentItem(pos);
		
	
		// hide the navigation bar
		hideSystemUi=true;
		
        hideSystemUi();
				actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
				actionBar.setTitle("");				
	            actionBar.setDisplayHomeAsUpEnabled(true);

		        // register a listener for when the navigation bar re-appears
                
		        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(

		        new OnSystemUiVisibilityChangeListener() {

		        public void onSystemUiVisibilityChange(int visibility) {

		        if (visibility == 0) {

		        mHideHandler.postDelayed(mHideRunnable, 1000);

		        }

		        }
		        
		        });

	}
	
	
	private void hideSystemUi() {

		getWindow().getDecorView().setSystemUiVisibility(

		View.SYSTEM_UI_FLAG_LAYOUT_STABLE

		| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

		| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

		| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

		| View.SYSTEM_UI_FLAG_FULLSCREEN

		| View.SYSTEM_UI_FLAG_IMMERSIVE);

		}
    Handler mHideHandler = new Handler();

	Runnable mHideRunnable = new Runnable() {

	public void run() {

	hideSystemUi();

	}

	};
	
	@Override
    protected void onResume() {
        super.onResume();
              if(hideSystemUi == true){
            	 

            	    	hideSystemUi();

            	    	}
              }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class DescAdapter extends FragmentStatePagerAdapter {
		public DescAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return feed.getItemCount();
		}

		@Override
		public Fragment getItem(int position) {

			ArticleFragment frag = new ArticleFragment();

			Bundle bundle = new Bundle();
			bundle.putSerializable("feed", feed);
			bundle.putInt("pos", position);
			frag.setArguments(bundle);

			return frag;

		}

	}

}
