package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.iven.lfflfeedreader.utils.Preferences;
import com.iven.lfflfeedreader.R;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    //Home ListView
    RSSFeed feed;
    ListView list;
    CustomListAdapter adapter;

    //Dynamic ListView
    private List<String> mItems;
    private List<String> mItems2;
    SQLiteDatabase mydb;
    CustomDynamicAdapter adapter_dynamic;
    ListView listfeed;
    String feedcustom;
    String feedcustom2;

    //Others
    Intent intent;
    SwipeRefreshLayout swiperefresh;

    //menu items
    Menu menu;
    MenuItem addfeed;
    MenuItem default_feeds;
    MenuItem xda;

    //Navigation drawer
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    //the default feed
    String feedURL = SplashActivity.default_feed_value;

    //create the toolbar's menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

		getMenuInflater().inflate(R.menu.activity_main, menu);

        //initialize add feed menu items
        addfeed = menu.findItem(R.id.addfeed);

        //xda menu item
        xda = menu.findItem(R.id.xda2);

        //built in feeds
        default_feeds = menu.findItem(R.id.default_feeds);

        return super.onCreateOptionsMenu(menu);

    }

	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //apply preferences

        //apply activity's theme if dark theme is enabled
        Preferences.applyTheme(this);

        //set the navbar tint if the preference is enabled
        if (Build.VERSION.SDK_INT >= 21){
        if (Preferences.navTintEnabled(getBaseContext())) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.primary));
        }
        }

        //set LightStatusBar
        if (Build.VERSION.SDK_INT >= 23) {
            if (Preferences.applyLightIcons(getBaseContext())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        //set the view
        setContentView(R.layout.home);

        //initialize the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

                                //close DrawerLayout
                                closeDrawer();

                                //open Google Play Store page
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader");
                                i.putExtra(android.content.Intent.EXTRA_SUBJECT, ("Lffl Feed Reader"));
                                startActivity(Intent.createChooser(i, getString(R.string.share)));
                        }

                        //rate button
                        switch (item.getItemId()) {
                            case R.id.rate:

                                //close DrawerLayout
                                closeDrawer();

                                //call rate method
                                rate(list);
                        }

                        switch (item.getItemId()) {
                            case R.id.mail:

                                //close DrawerLayout
                                closeDrawer();

                                //send a mail to us
                                intent = new Intent(android.content.Intent.ACTION_SEND);
                                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ivenfeedreader@outlook.com"});
                                intent.setType("message/rfc822");
                                if (intent != null) {
                                    startActivity(Intent.createChooser(intent, getString(R.string.emailC)));
                                }

                        }

                        //built in feeds
                        //open them using openNewFeed() method
                        switch (item.getItemId()) {
                            case R.id.noobs:

                                //open the feed on background thread calling openNewFeed method and refresh the ListView
                                openNewFeed("http://feeds.feedburner.com/NoobslabUbuntu/linuxNewsReviewsTutorialsApps");

                                //set feedURL calling setFeedString method, it is important if we want working swipe refresh listener
                                setFeedString("http://feeds.feedburner.com/NoobslabUbuntu/linuxNewsReviewsTutorialsApps");

                        }

                        switch (item.getItemId()) {
                            case R.id.softpedia:

                                //open Softpedia feed
                                openNewFeed("http://feeds.feedburner.com/SoftpediaNews/Linux");
                                setFeedString("http://feeds.feedburner.com/SoftpediaNews/Linux");
                        }

                        switch (item.getItemId()) {
                            case R.id.web8:

                                //open Web Upd8 feed
                                openNewFeed("http://feeds.feedburner.com/webupd8/YqnT");
                                setFeedString("http://feeds.feedburner.com/webupd8/YqnT");
                        }

                        switch (item.getItemId()) {
                            case R.id.omg:

                                //open OMG! Ubuntu! feed
                                openNewFeed("http://feeds.feedburner.com/d0od");
                                setFeedString("http://feeds.feedburner.com/d0od");
                        }

                        switch (item.getItemId()) {
                            case R.id.police:

                                //open Android Police feed
                                openNewFeed("http://feeds.feedburner.com/androidpolice/wszl");
                                setFeedString("http://feeds.feedburner.com/androidpolice/wszl");
                        }

                        switch (item.getItemId()) {
                            case R.id.androidcommunity:

                                //open Android Police feed
                                openNewFeed("http://androidcommunity.com/feed/");
                                setFeedString("http://androidcommunity.com/feed/");
                        }


                        //this is the button to add feed
                        switch (item.getItemId()) {
                            case R.id.xda:

                                //open default feed (xda)
                                openNewFeed("http://feeds.feedburner.com/xdadevs");
                                setFeedString("http://feeds.feedburner.com/xdadevs");
                        }

                        switch (item.getItemId()) {

                            case R.id.option:

                                //close DrawerLayout
                                closeDrawer();

                                //open Settings Activity
                                Intent ii = new Intent(ListActivity.this, InfoActivity.class);
                                startActivity(ii);
                        }

                        //this is the button to add feed
                        switch (item.getItemId()) {
                            case R.id.addfeed:

                                //call addFeed method
                                addFeed();
                        }

                        //this is the button to add feed
                        switch (item.getItemId()) {
                            case R.id.xda2:

                                //open default feed (xda)
                                openNewFeed("http://feeds.feedburner.com/xdadevs");
                                closeDrawer();
                                setFeedString("http://feeds.feedburner.com/xdadevs");
                        }

                        return false;
                    }
                });

        //initialize our navigation drawer

        //initialize the Drawer Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Tie DrawerLayout events to the ActionBarToggle:
        //it adds the hamburger icon
        //and handle the drawer slide event
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name) {

            //Called when the drawer is opened
        public void onDrawerOpened(View drawerView) {

            super.onDrawerOpened(drawerView);

            //change toolbar title to 'Add a feed'
            toolbar.setTitle(getResources().getString(R.string.feedmenu));

            //show the add feed menu option
            showAddFeed();

        }

            //Called when the drawer is closed
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            //change toolbar title to the app's name
            toolbar.setTitle(getResources().getString(R.string.app_name));

            //hide the add feed menu option
            hideAddFeed();

        }

        };

        //this handle the hamburger animation
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //initialize swipe to refresh layout
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //set on refresh listener
        swiperefresh.setOnRefreshListener(this);

        //set the default color of the arrow
        swiperefresh.setColorSchemeResources(R.color.refresh_color);


        //set the articles ListView and the dynamic ListView for custom feeds

        //let's start with the home's ListView

        //initialize the main ListView where items will be added
        list=(ListView) findViewById(android.R.id.list);

        //initialize the feeds items
        feed = (RSSFeed) getIntent().getExtras().get("feed");

        //set the main ListView custom adapter
        adapter=new CustomListAdapter(this);

        list.setAdapter(adapter);

        //handle main ListView clicks
        list.setOnItemClickListener(new

                                            OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
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
                                            }
        );

        //Dynamic ListView and database handling

        //initialize the dynamic ListView
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
        listfeed.setOnItemClickListener(new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?>arg0,View arg1,int arg2,
        long arg3){

        //we get the url of the item at the selected position
        //and set it
        setFeedString(mItems.get(arg2));

        //we send the url through intents to splash activity using openNewFeed method
        openNewFeed(feedURL);

        }
        });

        //set on item long click to delete custom feeds
            listfeed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

            {

                @Override
                public boolean onItemLongClick (AdapterView < ? > parent, View view,
                final int datposition, long arg3){

                //Disable ListView clicks to avoid unwanted clicks while deleting feeds
                listfeed.setEnabled(false);

                //on long click we create a new alert dialog
                AlertDialogWrapper.Builder alert = new AlertDialogWrapper.Builder(
                        ListActivity.this);

                alert.setTitle(R.string.deletedialogtitle);
                alert.setMessage(getResources().getString(R.string.deletedialogquestion) + " '" + mItems2.get(datposition) + "' ?");
                alert.setPositiveButton(R.string.deleteok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //on positive click we delete the feed from selected position

                        //we're gonna delete them from the db calling this method:
                        removedatFeed(datposition);

                        //enable ListView clicks
                        listfeed.setEnabled(true);

                    }
                });

                    //when You select by long clicking a feed the ListView becomes disabled
                    //the dialog is dismissable when there is a touch outside the dialog's bounds
                    //so, if You click outside th dialog area You won't be able to click on ListView
                    //items previously disabled
                    //we can override onCancel method to make ListView clickable again
                    alert.setOnCancelListener(
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    //enable ListView clicks
                                    listfeed.setEnabled(true);

                                }
                            }

                );

                //on negative button we dismiss the dialog
                    alert.setNegativeButton(R.string.deleteno, new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //dismiss the dialog
                            dialog.dismiss();

                            //enable ListView clicks
                            listfeed.setEnabled(true);

                        }
                });

                    alert.show();
                return false;
            }
            }

            );
        }

    //method to add feeds inside the db and the dynamic ListView
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

                        //and we update the ListView to add the new item
                        adapter_dynamic.notifyDataSetChanged();
                        listfeed.setAdapter(adapter_dynamic);

                        //populate the tables of the sqlite database with these values
                        // here we store names and urls that will be used on app's
                        //resume to populate the ListView

                        //fill the urls column
                        mydb.execSQL("insert into feedslist (url) values(?);", new String[]{feedcustom});

                        //fill the names column
                        mydb.execSQL("insert into subtitleslist (name) values(?);", new String[]{feedcustom2});

                    }

                }).show();
                    }

    //method to remove feeds inside the db and the dynamic ListView
    public void removedatFeed(int pos){

        //on positive click we delete the feed from selected position

        //we're gonna delete them from the db
        //how?

        //using a cursor we select items from the two tables
        Cursor cursor = mydb.rawQuery("SELECT * FROM feedslist;", null);
        Cursor cursor2 = mydb.rawQuery("SELECT * FROM subtitleslist;", null);

        //we set null values for url and feed name at selected position, we're going
        //to set these values dynamically to avoid "column index out of range" errors

        String url = "";
        String name = "";

        //set url
        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                //we get items at selected position
                url = mItems.get(pos);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //set feed name
        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //we get items at selected position
                name = mItems2.get(pos);
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
        String[] whereArgs_url = new String[]{String.valueOf(url)};
        String[] whereArgs_name = new String[]{String.valueOf(name)};

        //delete 'em all
        mydb.delete(table1, whereClause_url, whereArgs_url);
        mydb.delete(table2, whereClause_feed, whereArgs_name);

        //remove items from the dynamic ListView

        //for url
        mItems.remove(pos);

        //for feed name
        mItems2.remove(pos);

        //and update the dynamic list
        //don't move this method above the db deletion method or
        //you'll get javalangindexoutofboundsexception-invalid-index error
        adapter_dynamic.notifyDataSetChanged();
        adapter_dynamic.notifyDataSetInvalidated();
        listfeed.setAdapter(adapter_dynamic);
    }

    //this is the rate button
	public void rate(View view) {
		  Intent intent = new Intent(Intent.ACTION_VIEW);
		  intent.setData(Uri.parse("market://details?id=com.iven.lfflfeedreader"));
		  startActivity(intent);
		}

    //create a pending Runnable that runs in background to close the drawer smoothly
    //and to remove menu items from the toolbar
    public void closeDrawer() {
        final DrawerLayout mDrawerLayout;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                hideAddFeed();

            }
        }, 200);
        }

    //this is the method to open a new feed rss
    //the feed is parsed and the ListView will
    //be updated
    public void openNewFeed(final String datfeed) {

        //close the navigation drawer
        closeDrawer();

        //show swipe refresh
        swiperefresh.setRefreshing(true);

        // Detect if there's a connection issue or not
        ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // If there's a connection problem stop refreshing and show message
        if (cM.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT);
            toast.show();
            swiperefresh.setRefreshing(false);

        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DOMParser tmpDOMParser = new DOMParser();
                    feed = tmpDOMParser.parseXml(datfeed);
                    ListActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (feed != null && feed.getItemCount() > 0) {
                                adapter.notifyDataSetChanged();

                                //close swipe refresh
                                swiperefresh.setRefreshing(false);
                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }

    //this is the method to refresh the feed items and the list view
    //the xml is parsed again and if the number of the items is >0
    //new items will be added on top of the list activity's ListView
	public void onRefresh() {

        // Detect if there's a connection issue or not
        ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // If there's a connection problem stop refreshing and show message
        if (cM.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT);
            toast.show();
            swiperefresh.setRefreshing(false);

        } else {

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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

    }

    //this is the custom list adapter for the home ListView
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
		public View getView(int pos, View convertView, ViewGroup parent) {

			Activity activity = ListActivity.this;
			View listItem = convertView;

			if (listItem == null) {

                //set the main ListView's layout
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

            //if the preference is enabled remove the linear layout containing the imageview
            if (Preferences.imagesRemoved(getBaseContext())) {

                LinearLayout linearLayout = (LinearLayout) listItem.findViewById(R.id.layout);
                linearLayout.removeAllViewsInLayout();

            }

            //else, load the image
            //if getImage() method fails (i.e when img is in content:encoded) load image2
            else if (feed.getItem(pos).getImage().isEmpty()) {

                //use glide to load the image into the imageview (lfflimage)
                Glide.with(activity).load(feed.getItem(pos).getImage2())

                            //set a placeholder image
                            .placeholder(R.drawable.image_area)

                            //disable cache to avoid garbage collection that may produce crashes
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(lfflImage);

                //else use image
            }
            else
            {

                Glide.with(activity).load(feed.getItem(pos).getImage())

                        //set a placeholder image
                        .placeholder(R.drawable.image_area)

                                //disable cache to avoid garbage collection that may produce crashes
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(lfflImage);
            }

            return listItem;
        }
    }


    //this is the custom dynamic adapter for the custom feeds ListView
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

                //set the main ListView's layout
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

    private void hideAddFeed()
    {

        //hide addfeed menu item
        addfeed.setVisible(false);

        if (Preferences.builtfeedsEnabled(getBaseContext())) {

            //hide built in feeds menu if enabled
            default_feeds.setVisible(false);

        } else {

            //hide xda menu item
            xda.setVisible(false);
        }

    }

    private void showAddFeed() {

        //show addfeed menu item
        addfeed.setVisible(true);

        if (Preferences.builtfeedsEnabled(getBaseContext())) {

            //show built in feeds menu if enabled
            default_feeds.setVisible(true);
        } else {

            //show xda menu item
            xda.setVisible(true);
        }
    }

    //method to set the feed string on feed click
    private void setFeedString(String feed_string) {

         feedURL = feed_string;
        }
    }

