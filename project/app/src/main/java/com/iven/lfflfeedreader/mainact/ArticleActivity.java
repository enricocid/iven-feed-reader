package com.iven.lfflfeedreader.mainact;

import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

@SuppressLint("InlinedApi")
public class ArticleActivity extends AppCompatActivity {

    //feed
	RSSFeed feed;

    //position
	int pos;

    //ContextThemeWrapper
    ContextThemeWrapper themewrapper;

    //context
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

        //apply preferences

        //apply activity's theme if dark theme is enabled
        themewrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());
        Preferences.applyTheme(themewrapper, getBaseContext());


        //set the navbar tint if the preference is enabled
        Preferences.applyNavTint(this, getBaseContext());

        //set LightStatusBar
        Preferences.applyLightIcons(this);

        //set the immersive mode (only for >= KitKat) if the preference is enabled
        Preferences.applyImmersiveMode(this);

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
				if (hasFocus) {
					Preferences.applyImmersiveMode(this);
				}
			}
	}


