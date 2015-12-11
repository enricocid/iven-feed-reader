package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.infoact.ChangelogDialog;
import com.iven.lfflfeedreader.infoact.CreditsDialog;
import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.iven.lfflfeedreader.utils.Preferences;
import com.iven.lfflfeedreader.R;
import com.melnykov.fab.FloatingActionButton;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

	  private static final long DRAWER_CLOSE_DELAY_MS = 250;
	  private static final String NAV_ITEM_ID = "navItemId";
	  private final Handler mDrawerActionHandler = new Handler();
	  private DrawerLayout mDrawerLayout;
      private DrawerLayout mDrawerRight;
      private ActionBarDrawerToggle mDrawerToggle;
	  private int mNavItemId;
    private List<String> mItems;
    private List<String> mItems2;

    SQLiteDatabase mydb;
	RSSFeed feed;
	ListView list;
	CustomListAdapter adapter;
    CustomDynamicAdapter adapter_dynamic;
    String feedURL = SplashActivity.value;
	Intent intent;
	SwipeRefreshLayout swiperefresh;
    ListView listfeed;
    String feedcustom;
    String feedcustom2;

    //create the toolbar's menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
        Preferences.applyTheme(this);

        //set the navbar tint if the preference is enabled
        if (Build.VERSION.SDK_INT >= 21){
        if (Preferences.navTintEnabled(getBaseContext())) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.primary));
        }
        }

        //set the view
        setContentView(R.layout.iven_feed_list);

        //initialize the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //set the toolbar
        setSupportActionBar(toolbar);

        //set the toolbar's title
        toolbar.setTitle(R.string.app_name);

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //share button using share intent
                        switch (item.getItemId()) {
                            case R.id.share_option:
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader");
                                i.putExtra(android.content.Intent.EXTRA_SUBJECT, ("Lffl Feed Reader"));
                                startActivity(Intent.createChooser(i, getString(R.string.share)));
                                return true;
                        }

                        //rate button
                        switch (item.getItemId()) {
                            case R.id.rate:
                                rate(list);
                                return true;
                        }

                        //delete cache
                        switch (item.getItemId()) {
                            case R.id.cache:
                                clearApplicationData();
                                return true;
                        }

                        //this the plus button on toolbar to open the right-to-left drawer layout
                        switch (item.getItemId()) {
                            case R.id.addfeed:
                                if (mDrawerLayout.isDrawerOpen(mDrawerRight)) {
                                    mDrawerLayout.closeDrawer(mDrawerRight);
                                } else {
                                    mDrawerLayout.openDrawer(mDrawerRight);
                                }
                                return true;
                        }

                        return false;
                    }
                });

        //initialize our navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerRight = (DrawerLayout) findViewById(R.id.drawer_right);

        //setup drawer's view
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        if (null == savedInstanceState) {
            mNavItemId = R.id.option;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        //Tie DrawerLayout events to the ActionBarToggle:
        //it adds the hamburger icon
        //and handle the drawer slide event
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerToggle.syncState();
       //! The handle of menu items is explained at the end of this java


        //initialize swipe to refresh layout
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //set on refresh listener
        swiperefresh.setOnRefreshListener(this);

        //set the default color of the arrow
        swiperefresh.setColorSchemeResources(R.color.refresh_color);

        //initialize the dynamic listview
        listfeed =(ListView) findViewById(R.id.listfeed);

        //initialize a sqlite database
        //here we open or create a sqlite database where we are going to add 2 tables with the feeds values
        mydb = ListActivity.this.openOrCreateDatabase("feedsDB", MODE_PRIVATE, null);

        //create a table called feedslist with a column called "url" where items (the feeds urls) will be stored
        mydb.execSQL("CREATE TABLE IF NOT EXISTS feedslist (id INTEGER PRIMARY KEY AUTOINCREMENT,url varchar);");

        //create a table called subtitles list with a column called "name" where items (the feeds names) will be stored
        mydb.execSQL("CREATE TABLE IF NOT EXISTS subtitleslist (id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar);");

        //using a cursor we're going to read the tables
        final Cursor cursor2 = mydb.rawQuery("SELECT * FROM feedslist;", null);
        final Cursor cursor3 = mydb.rawQuery("SELECT * FROM subtitleslist;", null);

        //we create two new array list to be populated with the tables items (names & urls)

        //for feeds urls
        mItems = new ArrayList<>();

        //for feeds names
        mItems2 = new ArrayList<>();

        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //add items from column "url" into dynamic list
                 mItems.add(cursor2.getString(cursor2.getColumnIndex("url")));
                cursor2.moveToNext();
            }
            cursor2.close();
        }

        if (cursor3 != null && cursor3.moveToFirst()) {

            while (!cursor3.isAfterLast()) {

                //add items from column "name" into dynamic list
                mItems2.add(cursor3.getString(cursor3.getColumnIndex("name")));
                cursor3.moveToNext();
            }
            cursor3.close();
        }

        //initialize the dynamic list array adapter, we set a template layout and the dynamic list formerly populated
        adapter_dynamic = new CustomDynamicAdapter(this, mItems, mItems2);

        //refresh the dynamic list
        //this is needed to update the dynamic list
        // when we delete items from the tables
        adapter_dynamic.notifyDataSetChanged();

        //set the custom adapter
        listfeed.setAdapter(adapter_dynamic);

        //handle the dynamic list items click
        listfeed.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //we get the url of the item at the selected position
                String feedvalue = mItems.get(arg2);

                //we send the url using intents to splash activity
                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                        ListActivity.this, SplashActivity.class));
                intent.putExtra("feedselected", feedvalue);

                //and start a new list activity with the selected feed
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    listfeed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
        final int datposition, long arg3) {

                //on long click we create a new alert dialog
                AlertDialogWrapper.Builder alert = new AlertDialogWrapper.Builder(
                        ListActivity.this);

                alert.setTitle(R.string.deletedialogtitle);
                alert.setMessage(getResources().getString(R.string.deletedialogquestion) + " '" + mItems2.get(datposition) + "' ?");
                alert.setPositiveButton(R.string.deleteok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //on positive click we delete the feed from selected position

                        //we're gonna delete them from the db
                        //how?

                        //using a cursor we select items from the two tables
                        Cursor cursor =mydb.rawQuery("SELECT * FROM feedslist;", null);
                        Cursor cursor2 =mydb.rawQuery("SELECT * FROM subtitleslist;", null);

                        //we set null values for url and feed name at selected position, we're going
                        //to set these values dynamically to avoid "column index out of range" errors

                        String url = "";
                        String name = "";

                        //set url
                        if (cursor != null && cursor.moveToFirst()) {

                            while (!cursor.isAfterLast()) {

                                //we get items at selected position
                                url = mItems.get(datposition);
                                cursor.moveToNext();
                            }
                            cursor.close();
                        }

                        //set feed name
                        if (cursor2 != null && cursor2.moveToFirst()) {

                            while (!cursor2.isAfterLast()) {

                                //we get items at selected position
                                name = mItems2.get(datposition);
                                cursor2.moveToNext();
                            }
                            cursor2.close();
                        }

                        //set the names of the two tables
                        String table1 = "feedslist";
                        String table2 = "subtitleslist";

                        //set where clause
                        String whereClause_url = "url" + "=?";
                        String whereClause_feed = "name" + "=?";

                        //set the where arguments
                        String[] whereArgs_url = new String[] { String.valueOf(url) };
                        String[] whereArgs_name = new String[] { String.valueOf(name) };

                        //delete 'em all
                        mydb.delete(table1, whereClause_url, whereArgs_url);
                        mydb.delete(table2, whereClause_feed, whereArgs_name);

                        //remove items from the dynamic listview

                        //for url
                        mItems.remove(datposition);

                        //for feed name
                        mItems2.remove(datposition);

                        //and update the dynamic list
                        //don't move this method above the db deletion method or
                        //you'll get javalangindexoutofboundsexception-invalid-index error
                        adapter_dynamic.notifyDataSetChanged();
                        adapter_dynamic.notifyDataSetInvalidated();
                        listfeed.setAdapter(adapter_dynamic);
                    }
                });

            //on negative button we dismiss the dialog
            alert.setNegativeButton(R.string.deleteno, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
            return false;
        }
    });

        //initialize the fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //attach the fab on listview to react to scroll events and
        //allow the fab to autohide when needed
        fab.attachToListView(listfeed);

        //this the method to handle fab click to open an input dialog
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeed();
            }
        };

        //set fab's on click listener
        fab.setOnClickListener(listener);

        //initialize the main listview where items will be added
        list = (ListView) findViewById(android.R.id.list);

        //initialize the feeds items
        feed = (RSSFeed) getIntent().getExtras().get("feed");

        //set the main listview custom adapter
		adapter = new CustomListAdapter(this);

		list.setAdapter(adapter);

        //handle main listview clicks
        list.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick (AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				int pos = arg2;

            //on item click we send the feed to article activity
            //using intents
				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
                Intent intent = new Intent(ListActivity.this,
                            ArticleActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", pos);

            //and we start the article activity to read the story
                    startActivity(intent);
                                    }
		});
	}

    //method to add feeds inside the db and the dynamic listview
    public void addFeed(){

        new MaterialDialog.Builder(this)

                .title(R.string.adddialogtitle)
                .negativeText(android.R.string.no)
                .positiveText(android.R.string.ok)
                .customView(R.layout.add_feed_layout, false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        //on click ok we get the text inputs:

                        //get the feeds urls from the input and add to array list
                        final EditText edt1 = (EditText) dialog.findViewById(R.id.txt1);
                        feedcustom = edt1.getText().toString();
                        mItems.add(feedcustom);

                        //get the feeds names from the input and add to array list
                        final EditText edt2 = (EditText) dialog.findViewById(R.id.txt2);
                        feedcustom2 = edt2.getText().toString();
                        mItems2.add(feedcustom2);

                        //and we update the listview to add the new item
                        adapter_dynamic.notifyDataSetChanged();
                        listfeed.setAdapter(adapter_dynamic);

                        //populate the tables of the sqlite database with these values
                        // here we store names and urls that will be used on app's
                        //resume to populate the listview

                        //fill the urls column
                        mydb.execSQL("insert into feedslist (url) values(?);", new String[]{feedcustom});

                        //fill the names column
                        mydb.execSQL("insert into subtitleslist (name) values(?);", new String[]{feedcustom2});

                    }

                        }).show();
                    }

    //this is the method to delete the app's cache
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    //this is the rate button
	public void rate(View view) {
		  Intent intent = new Intent(Intent.ACTION_VIEW);
		  intent.setData(Uri.parse("market://details?id=com.iven.lfflfeedreader"));
		  startActivity(intent);
		}

    //this is the method to refresh the feed items and the list view
    //the xml is parsed again and if the number of the items is >0
    //new items will be added on top of the list activity's listview
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

    //this the custom list adapter for the main listview
    //we use a custom adapter to set a custom layout for items
class CustomListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public CustomListAdapter(ListActivity activity) {

            //initialize layout inflater
            layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

        //get items count
		@Override
		public int getCount() {
			return feed.getItemCount();
		}

        //get items position
		@Override
		public Object getItem(int position) {
			return position;
		}

        //get items id at selected position
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

                //set the main listview's layout
				listItem = layoutInflater.inflate(R.layout.items, parent, false);
            }

            //get the chosen items text size from preferences
            float size = Preferences.resolveTextSizeListResId(getBaseContext());

            //initialize the dynamic items (the title, subtitle)
            TextView lfflTitle = (TextView) listItem.findViewById(R.id.title);
            TextView pubDate = (TextView) listItem.findViewById(R.id.date);

            //dynamically set title and subtitle according to the feed data

            //title
            lfflTitle.setText(feed.getItem(pos).getTitle());

            //subtitle= publication date
            pubDate.setText(feed.getItem(pos).getDate());

            //set the list items text size from preferences
            //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
            // size = the text size from preferences
            lfflTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            pubDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, size -2);

            //initialize the imageview
            ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);

            //use glide to load the image into the imageview (lfflimage)
                    Glide.with(activity).load(feed.getItem(pos).getImage())

                            //set a placeholder image
                            .placeholder(R.drawable.image_area)

                            //disable cache to avoid garbage collection that may produce crashes
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(lfflImage);

            return listItem;
        }
    }

    //this the custom dynamic adapter for the custom feeds listview
    //we use a custom adapter to set a custom layout for items (names + urls)

    class CustomDynamicAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        //for feeds urls
        private List<String> mList;

        //for feeds names
        private List<String> mList2;

        public CustomDynamicAdapter(ListActivity activity, List<String> list, List<String> list2) {

            //for urls
            mList = list;

            //for names
            mList2 = list2;

            //initialize layout inflater
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //get items (urls) count
        @Override
        public int getCount() {
            return mList.size();
        }

        //get items (urls) position
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        //get items (urls) id at selected position
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;

            if (listItem == null) {

                //set the main listview's layout
                listItem = layoutInflater.inflate(R.layout.dynamic_items, parent, false);
            }

            //initialize the dynamic items (titles+ urls)
            TextView Title = (TextView) listItem.findViewById(R.id.title_dyn);
            TextView url = (TextView) listItem.findViewById(R.id.subtitle_dyn);

            //dynamically set title and subtitle according to the feed data

            //set feeds urls
            url.setText(mList.get(position));

            //set feeds titles
            Title.setText(mList2.get(position));

            return listItem;
        }
    }

    //this is where we handle the navigation drawer clicks
    //menu items are set directly in iven_feed_list layout!
    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        //set the clicked memu item checked
        menuItem.setChecked(true);

        //get all menu items Ids
        mNavItemId = menuItem.getItemId();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (menuItem.getItemId()) {

                    //open Info Activity
                    case R.id.option:
                        Intent ii = new Intent(ListActivity.this, InfoActivity.class);
                        startActivity(ii);
                }
                switch (menuItem.getItemId()) {

                    //open github page using intents
                    case R.id.source_code:
                        Intent ii4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/enricocid/iven-feed-reader"));
                        startActivity(ii4);
                }

                switch (menuItem.getItemId()) {
                    case R.id.noobs:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/NoobslabUbuntu/linuxNewsReviewsTutorialsApps");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.phoronix:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/phoronix/RbeQ");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.softpedia:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/SoftpediaNews/Linux");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.distrowatch:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/distrowatch/RUwq");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.web8:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/webupd8/YqnT");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.omg:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/d0od");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.police:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/androidpolice/wszl");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.xda:

                        //start a new splash activity for the selected feed
                        //send the feed url to splash activity using intents
                        final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                                ListActivity.this, SplashActivity.class));
                        intent.putExtra("feedselected", "http://feeds.feedburner.com/xdadevs");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                }

                switch (menuItem.getItemId()) {
                    case R.id.changelog:

                        //showChangelog method is called
                        showChangelog();
                }

                switch (menuItem.getItemId()) {
                    case R.id.about_option_more:
                        //showInfo method is called
                        showInfo();
                }

                switch (menuItem.getItemId()) {
                    case R.id.mail:

                        //send a mail to us
                        intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ivenfeedreader@outlook.com"});
                        intent.setType("message/rfc822");
                        if (intent != null) {
                            startActivity(Intent.createChooser(intent, getString(R.string.emailC)));
                        }

                }

            }

        }, DRAWER_CLOSE_DELAY_MS);
        return true;

    }

    //method to open the credits dialog
    private void showInfo() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0)
            accentColor = ContextCompat.getColor(getBaseContext(), R.color.accent_color);

        CreditsDialog.create(accentColor)
                .show(getSupportFragmentManager(), "credits");
    }

    //method to open the changelog dialog
    private void showChangelog() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0)
            accentColor = ContextCompat.getColor(getBaseContext(), R.color.accent_color);

        ChangelogDialog.create(accentColor)
                .show(getSupportFragmentManager(), "changelog");

    }
}

