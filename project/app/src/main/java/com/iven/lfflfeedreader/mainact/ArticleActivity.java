package com.iven.lfflfeedreader.mainact;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.iven.lfflfeedreader.utils.Preferences;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;

@SuppressLint("InlinedApi")
public class ArticleActivity extends AppCompatActivity {

	RSSFeed feed;
	int pos;
	private DescAdapter adapter;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Preferences.applyTheme(this);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.article_activity);

		feed = (RSSFeed) getIntent().getExtras().get("feed");
		pos = getIntent().getExtras().getInt("pos");
		adapter = new DescAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(pos);

	}

	public class DescAdapter extends FragmentStatePagerAdapter {
						public DescAdapter(FragmentManager fm) {

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
}
