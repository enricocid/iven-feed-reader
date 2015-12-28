package com.iven.lfflfeedreader.mainact;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

@SuppressLint("InlinedApi")
public class ArticleActivity extends AppCompatActivity {

    //feed
	RSSFeed feed;

    //position
	int pos;

    //context
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
		Preferences.applyTheme(this);

		//set the view
		setContentView(R.layout.article_activity);

		//get the feed using intents
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		pos = getIntent().getExtras().getInt("pos");

		//get the context
		context = getBaseContext();

        //initialize ViewPager and the adapter onCreate to avoid AS warning
        //'field can be converted to a local variable'
		ViewPager pager;
		PagerAdapter mPagerAdapter;

		//set the viewpager that allows to swipe through articles
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(mPagerAdapter);
        pager.setCurrentItem(pos);
        pager.setClipToPadding(false);

		//set the navbar tint if the preference is enabled
		if (Build.VERSION.SDK_INT >= 21){
		if (Preferences.navTintEnabled(this)) {
			getWindow().setNavigationBarColor(ContextCompat.getColor(getBaseContext(),  R.color.primary));
		}

            //set LightStatusBar
            if (Build.VERSION.SDK_INT >= 23) {
                if (Preferences.applyLightIcons(getBaseContext())) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }

            //set the immersive mode (only for >= KitKat) if the preference is enabled
            if (Build.VERSION.SDK_INT >= 19){
                if (Preferences.immersiveEnabled(this)) {

                    //immersive mode
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                }
            }
}
    }

    //Viewpager custom adapter, we extend FragmentStatePagerAdapter to handle a large number of dynamic items
    //FragmentStatePagerAdapter should be used when we have to use dynamic fragments
    //as their data could be stored in the savedInstanceState.
    // Also it wont affect the performance even if there are large number of fragments.

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

						public MyPagerAdapter(FragmentManager fm) {

							super(fm);
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

		@Override
		public int getCount() {
			return feed.getItemCount();
		}
			}

	//(only for >= KitKat)
	//fix Immersive mode navigation becomes sticky after minimise-restore
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (Build.VERSION.SDK_INT >= 19){
			if (Preferences.immersiveEnabled(this)) {
				if (hasFocus) {
					getWindow().getDecorView().setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
									| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
									| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                                    //Sticky flag - This is the UI you see if you use the IMMERSIVE_STICKY flag, and the user
                                    //swipes to display the system bars. Semi-transparent bars temporarily appear
                                    //and then hide again
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
			}

		}
	}
}


