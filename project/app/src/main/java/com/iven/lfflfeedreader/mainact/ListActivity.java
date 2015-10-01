package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.bumptech.glide.Glide;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.infoact.ChangelogDialog;
import com.iven.lfflfeedreader.infoact.InfoActivity;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

	  private static final long DRAWER_CLOSE_DELAY_MS = 250;
	  private static final String NAV_ITEM_ID = "navItemId";
	  private final Handler mDrawerActionHandler = new Handler();
	  private DrawerLayout mDrawerLayout;
	  private FrameLayout lfflhead;
	  private ActionBarDrawerToggle mDrawerToggle;
	  private int mNavItemId;

	RSSFeed feed;
	ListView list;
	CustomListAdapter adapter;
	String feedURL;
	Intent intent;
	SwipeRefreshLayout swiperefresh;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

		@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.lffl_feed_list);
	      mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	         if (null == savedInstanceState) {
	           mNavItemId = R.id.about_option;
	         } else {
	           mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
	         }

	         NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
	         navigationView.setNavigationItemSelectedListener(this);
	      
	         navigationView.getMenu().findItem(mNavItemId).setChecked(true);
	         
	         lfflhead = (FrameLayout) findViewById(R.id.head);
	         lfflhead.setOnLongClickListener(new OnLongClickListener() {
				 @Override
				 public boolean onLongClick(View v) {
					 showTorvalds();
					 return true;
				 }
			 });

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
        toolbar.setTitle(R.string.app_name);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
            R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                    	switch (item.getItemId()) {
                    	case R.id.share_option:
                    	Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader");
                        i.putExtra(android.content.Intent.EXTRA_SUBJECT, ("Lffl Feed Reader"));
                        startActivity(Intent.createChooser(i, getString(R.string.share)));
                        return true;
                    }

            			switch (item.getItemId()) {
            			case R.id.rate:
            				rate(list);
            				return true;
            			}
						return false;
                    }	
            	});

		feedURL = new SplashActivity().LFFLFEEDURL;

		feed = (RSSFeed) getIntent().getExtras().get("feed");

		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swiperefresh.setOnRefreshListener(this);
		swiperefresh.setColorSchemeResources(R.color.lffl2);
		
		list = (ListView) findViewById(android.R.id.list);
		adapter = new CustomListAdapter(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2;

				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				Intent intent = new Intent(ListActivity.this,
						ArticleActivity.class);
				intent.putExtras(bundle);
				intent.putExtra("pos", pos);
				startActivity(intent);

			}
		});

	}

	private void showTorvalds() {

		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, R.string.easter, Toast.LENGTH_SHORT);
	    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    toast.show();

	}

	public void rate(View view) {
		  Intent intent = new Intent(Intent.ACTION_VIEW);
		  intent.setData(Uri.parse("market://details?id=com.iven.lfflfeedreader"));
		  startActivity(intent);
		}
	
	public void onRefresh() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				DOMParser tmpDOMParser = new DOMParser();
				feed = tmpDOMParser.parseXml(feedURL);

				ListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (feed != null && feed.getItemCount() > 0) {
							adapter.notifyDataSetChanged();
							
							swiperefresh.setRefreshing(false);
						}
					}
				});
			}
		});
		thread.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.notifyDataSetChanged();
	}

	class CustomListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public CustomListAdapter(ListActivity activity) {

			layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {

			return feed.getItemCount();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Activity activity = ListActivity.this;
			View listItem = convertView;
			int pos = position;
			if (listItem == null) {
				listItem = layoutInflater.inflate(R.layout.items, null);
			}

			ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);
			TextView lfflTitle = (TextView) listItem.findViewById(R.id.title);

			TextView pubDate = (TextView) listItem.findViewById(R.id.date);
			Glide.with(activity).load(feed.getItem(pos).getImage())
							.into(lfflImage);

			lfflTitle.setText(feed.getItem(pos).getTitle());
			pubDate.setText(feed.getItem(pos).getDate());

			return listItem;
		}

	}

	@Override
	 public boolean onNavigationItemSelected(final MenuItem menuItem) {

	    menuItem.setChecked(true);
	    mNavItemId = menuItem.getItemId();
		mDrawerLayout.closeDrawer(GravityCompat.START);
		mDrawerActionHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				switch (menuItem.getItemId()) {
					case R.id.about_option:
						Intent ii = new Intent(ListActivity.this, InfoActivity.class);
						startActivity(ii);
				}
				switch (menuItem.getItemId()) {
					case R.id.cuties:
						Intent ii2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/itcuties/ITCutiesApp-1.0"));
						startActivity(ii2);
				}
				switch (menuItem.getItemId()) {
					case R.id.source_code:
						Intent ii4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/enricocid/lffl-feed-reader"));
						startActivity(ii4);
				}
				switch (menuItem.getItemId()) {
					case R.id.developer1:
						Intent ii5 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/member.php?u=4884893"));
						startActivity(ii5);
				}
				switch (menuItem.getItemId()) {
					case R.id.developer2:
						Intent ii6 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://disqus.com/by/enricodchem/"));
						startActivity(ii6);
				}
				switch (menuItem.getItemId()) {
					case R.id.changelog:
						showChangelog();
				}
				switch (menuItem.getItemId()) {
					case R.id.mail:
						intent = new Intent(android.content.Intent.ACTION_SEND);
						intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ivandorte@gmail.com"});
						intent.setType("message/rfc822");
						if (intent != null) {
							startActivity(Intent.createChooser(intent, getString(R.string.emailC)));
						}

				}
			}

			private void showChangelog() {
				int accentColor = ThemeSingleton.get().widgetColor;
				if (accentColor == 0)
					accentColor = getResources().getColor(R.color.lffl4);

				ChangelogDialog.create(accentColor)
						.show(getSupportFragmentManager(), "changelog");

			}
		}, DRAWER_CLOSE_DELAY_MS);
	    return true;
	    
    	}
		
	}
