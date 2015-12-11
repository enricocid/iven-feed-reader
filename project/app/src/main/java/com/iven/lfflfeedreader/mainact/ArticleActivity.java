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
import android.view.Window;
import android.view.WindowManager;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

@SuppressLint("InlinedApi")
public class ArticleActivity extends AppCompatActivity {

	RSSFeed feed;
	int pos;
	private ViewPager pager;
	private PagerAdapter mPagerAdapter;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//apply activity's theme if dark theme is enabled
		Preferences.applyTheme(this);

		//set the view
		setContentView(R.layout.article_activity);

		//get the feed using itents
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		pos = getIntent().getExtras().getInt("pos");

		//get the context
		context = getBaseContext();

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

							if (Preferences.WebViewEnabled(context)) {
								ArticleFragmentWebView fragwb = new ArticleFragmentWebView();
								Bundle bundle = new Bundle();
								bundle.putSerializable("feed", feed);
								bundle.putInt("pos", position);
								fragwb.setArguments(bundle);

								return fragwb;
							} else {
								ArticleFragment frag = new ArticleFragment();
								Bundle bundle = new Bundle();
								bundle.putSerializable("feed", feed);
								bundle.putInt("pos", position);
								frag.setArguments(bundle);

								return frag;

						}

                            }

		@Override
		public int getCount() {
			return feed.getItemCount();
		}
			}
    }




