package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.bumptech.glide.Glide;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.infoact.ChangelogDialog;
import com.iven.lfflfeedreader.infoact.CreditsDialog;
import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.iven.lfflfeedreader.utils.Preferences;

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
	  private ActionBarDrawerToggle mDrawerToggle;
	  private int mNavItemId;

	RSSFeed feed;
	ListView list;
	CustomListAdapter adapter;
    String feedURL = SplashActivity.value;
	Intent intent;
	SwipeRefreshLayout swiperefresh;
    String shithappens = "¯\\_(ツ)_/¯ shit happens man";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

		@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
			Preferences.applyTheme2(this);
		    setContentView(R.layout.iven_feed_list);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	         if (null == savedInstanceState) {
	           mNavItemId = R.id.option;
	         } else {
	           mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
	         }

	         NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
	         navigationView.setNavigationItemSelectedListener(this);
	      
	         navigationView.getMenu().findItem(mNavItemId).setChecked(true);

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

			if (Preferences.navTintEnabled(getBaseContext())) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.iven4));
            }

            feed = (RSSFeed) getIntent().getExtras().get("feed");

		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swiperefresh.setOnRefreshListener(this);
		swiperefresh.setColorSchemeResources(R.color.iven2);
		
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(ListActivity.this,
                            ArticleActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", pos);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(ListActivity.this,
						ArticleActivity_preKK.class);
				intent.putExtras(bundle);
				intent.putExtra("pos", pos);
				startActivity(intent);
                }
			}
		});

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
        adapter.notifyDataSetChanged();
        super.onDestroy();
	}

    @Override
    public void onResume(){
        adapter.notifyDataSetChanged();
        super.onResume();
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
				listItem = layoutInflater.inflate(R.layout.items, parent, false);
            }

            TextView lfflTitle = (TextView) listItem.findViewById(R.id.title);

            TextView pubDate = (TextView) listItem.findViewById(R.id.date);

            ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);

            if (feed.getItem(pos).getImage().isEmpty()) {

                LinearLayout linearLayout = (LinearLayout) listItem.findViewById(R.id.layout);
                linearLayout.removeAllViewsInLayout();

            } else {

                if (Preferences.imagesRemoved(getBaseContext())) {
                    LinearLayout linearLayout = (LinearLayout) listItem.findViewById(R.id.layout);
                    linearLayout.removeAllViewsInLayout();

                } else {
                    Glide.with(activity).load(feed.getItem(pos).getImage())
                            .asBitmap()
                            .into(lfflImage);
                }

            }

            lfflTitle.setText(feed.getItem(pos).getTitle());

            if (feed.getItem(pos).getAuthor() == null && feed.getItem(pos).getDate() == null) {

                pubDate.setText(shithappens);

            } else {

                pubDate.setText(feed.getItem(pos).getDate());
            }

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
                            case R.id.option:
                                Intent ii = new Intent(ListActivity.this, InfoActivity.class);
                                startActivity(ii);
                        }
                        switch (menuItem.getItemId()) {
                            case R.id.source_code:
                                Intent ii4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/enricocid/lffl-feed-reader"));
                                startActivity(ii4);
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.noobs:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/NoobslabUbuntu/linuxNewsReviewsTutorialsApps");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.phoronix:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/phoronix/RbeQ");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.softpedia:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/SoftpediaNews/Linux");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.distrowatch:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/distrowatch/RUwq");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.geek:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/UbuntuGeekNews");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.forge:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/howtoforge/LDMd");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.web8:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/webupd8/YqnT");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.omg:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/d0od");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.police:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/androidpolice/wszl");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.xda:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/xdadevs");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.techaeris:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/TechaerisAndroid");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.phonearena:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/phonearena/jApB");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.central:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/androidcentral/tDcB");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.authority:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/androidauthority/jsTn");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.lffl:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/lffl");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.chimeral:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/chimerarevo/tlEz");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.alexio:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/feedburner/jWXa");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.marcobox:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/marcosbox/Svdz");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.pit:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/androidpit/eYCt");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.androidiani:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/feedburner/doAd");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.chimeraa:

                                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                        ListActivity.this, SplashActivity.class));
                                intent.putExtra("feedselected", "http://feeds.feedburner.com/ChimeraRevo-NewsGuideERecensioniSulMondoDellaTecnologiaAndroid");
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.changelog:
                                showChangelog();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.about_option_more:
                                showInfo();
                        }

                        switch (menuItem.getItemId()) {
                            case R.id.mail:
                                intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"lfflfeedreader@hotmail.com"});
                                intent.setType("message/rfc822");
                                if (intent != null) {
                                    startActivity(Intent.createChooser(intent, getString(R.string.emailC)));
                                }

                        }

                    }

                    private void showChangelog() {
                        int accentColor = ThemeSingleton.get().widgetColor;
                        if (accentColor == 0)
                            accentColor = getResources().getColor(R.color.iven4);

                        ChangelogDialog.create(accentColor)
                                .show(getSupportFragmentManager(), "changelog");

                    }

                    private void showInfo() {
                        int accentColor = ThemeSingleton.get().widgetColor;
                        if (accentColor == 0)
                            accentColor = getResources().getColor(R.color.iven4);

                        CreditsDialog.create(accentColor)
                                .show(getSupportFragmentManager(), "credits");
                    }
                }, DRAWER_CLOSE_DELAY_MS);
	    return true;
	    
    	}
		
	}
