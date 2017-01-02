package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.domparser.RSSItem;
import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.iven.lfflfeedreader.utils.GlideUtils;
import com.iven.lfflfeedreader.utils.HomeUtils;
import com.iven.lfflfeedreader.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    //feed
    RSSFeed fFeed;
    RSSItem feedItem;
    String imageLink;
    String imageLink2;
    String feedTitle;
    String feedDate;

    //Home ListView
    ListView list;
    CustomListAdapter adapter;
    SQLiteDatabase mydb;
    CustomDynamicAdapter adapter_dynamic;
    ListView listfeed;
    String feedcustom;
    String feedcustom2;

    //Others
    SwipeRefreshLayout swiperefresh;

    //menu items
    Menu menu;
    MenuItem addfeed;

    //Connectivity manager
    ConnectivityManager cM;

    //Context theme wrapper
    ContextThemeWrapper themewrapper;

    //navigation drawer
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    //the default feed
    String feedURL = SplashActivity.default_feed_value;


    //Dynamic ListView
    private List<String> mUrls;
    private List<String> mFeeds;

    //create the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        getMenuInflater().inflate(R.menu.activity_main, menu);

        //initialize add feed menu items
        addfeed = menu.findItem(R.id.addfeed);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //initialize the feeds items
        fFeed = (RSSFeed) getIntent().getExtras().get("feed");

        //initialize connectivity manager
        cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //apply preferences

        //apply activity's theme if dark theme is enabled
        themewrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());
        Preferences.applyTheme(themewrapper, getBaseContext());

        //set the navbar tint if the preference is enabled
        Preferences.applyNavTint(this, getBaseContext(), R.color.accent_color);

        //set LightStatusBar
        Preferences.applyLightIcons(this);

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

                        final int mItemId = item.getItemId();

                        //share button using share intent
                        switch (mItemId) {
                            case R.id.share_option:

                                //close DrawerLayout
                                closeDrawer();

                                //open Google Play Store page
                                HomeUtils.shareGoogleStorePage(ListActivity.this);
                                break;

                            //rate button
                            case R.id.rate:

                                //close DrawerLayout
                                closeDrawer();

                                //call rate method
                                HomeUtils.rate(ListActivity.this);

                                break;

                            case R.id.mail:

                                //close DrawerLayout
                                closeDrawer();

                                HomeUtils.mailus(ListActivity.this);

                                break;

                            case R.id.option:

                                //close DrawerLayout
                                closeDrawer();

                                //open Settings Activity
                                Intent ii = new Intent(ListActivity.this, InfoActivity.class);
                                startActivity(ii);
                                break;

                            //this is the button to add feed
                            case R.id.addfeed:

                                //call addFeed method
                                addFeed();
                                break;
                        }

                        return false;
                    }
                });

        //initialize our navigation drawer

        //initialize the Drawer Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Tie DrawerLayout events to the ActionBarToggle:
        //it adds the hamburger icon and handle the drawer slide event
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name) {

            //Called when the drawer is opened
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

                //change toolbar title to 'Add a feed'
                toolbar.setTitle(getResources().getString(R.string.feedmenu));

                //show the add feed menu option
                HomeUtils.showAddFeed(addfeed);
            }

            //Called when the drawer is closed
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //change toolbar title to the app's name
                toolbar.setTitle(getResources().getString(R.string.app_name));

                //hide the add feed menu option
                HomeUtils.hideAddFeed(addfeed);
            }

        };

        //this handle the hamburger animation
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //initialize swipe to refresh layout
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //set on refresh listener
        swiperefresh.setOnRefreshListener(this);

        //set the default color of the arrow
        swiperefresh.setColorSchemeResources(R.color.refresh_color);

        //set the articles ListView and the dynamic ListView for custom feeds

        //let's start with the home's ListView

        //initialize the main ListView where items will be added
        list = (ListView) findViewById(android.R.id.list);

        //set the main ListView custom adapter
        adapter = new CustomListAdapter(this);

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
                                                    bundle.putSerializable("feed", fFeed);
                                                    Intent intent = new Intent(ListActivity.this,
                                                            ArticleActivity.class);
                                                    intent.putExtras(bundle);
                                                    intent.putExtra("pos", pos);

                                                    //and we start the article activity to read the story
                                                    startActivity(intent);
                                                }
                                            }
        );

        //dynamic ListView and database handling

        //initialize the dynamic ListView
        listfeed = (ListView) findViewById(R.id.listfeed);

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
        mUrls = new ArrayList<>();

        //for feeds names
        mFeeds = new ArrayList<>();

        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //add items from column "url" into dynamic list
                mUrls.add(cursor2.getString(cursor2.getColumnIndex("url")));
                cursor2.moveToNext();
            }
            cursor2.close();
        }

        if (cursor3 != null && cursor3.moveToFirst()) {

            while (!cursor3.isAfterLast()) {

                //add items from column "name" into dynamic list
                mFeeds.add(cursor3.getString(cursor3.getColumnIndex("name")));
                cursor3.moveToNext();
            }
            cursor3.close();
        }

        //initialize the dynamic list array adapter, we set a template layout and the dynamic list formerly populated
        adapter_dynamic = new CustomDynamicAdapter(this, mUrls, mFeeds);

        //refresh the dynamic list
        adapter_dynamic.notifyDataSetChanged();

        //set the custom adapter
        listfeed.setAdapter(adapter_dynamic);

        //handle the dynamic list items click
        listfeed.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //display the feed using openNewFeed method
                openNewFeed(mUrls.get(arg2));

            }
        });

        //set on item long click to delete custom feeds
        listfeed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

                                            {

                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                                               final int datposition, long arg3) {

                                                    //Disable ListView clicks to avoid unwanted clicks while deleting feeds
                                                    listfeed.setEnabled(false);

                                                    //on long click we create a new alert dialog

                                                    new AlertDialog.Builder(ListActivity.this)

                                                            .setTitle(R.string.deletedialogtitle)
                                                            .setMessage(getResources().getString(R.string.deletedialogquestion) + " '" + mFeeds.get(datposition) + "' ?")

                                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //on positive click we delete the feed from selected position

                                                                    //we're gonna delete them from the db calling this method:
                                                                    removedatFeed(datposition);

                                                                    //enable ListView clicks
                                                                    listfeed.setEnabled(true);
                                                                }
                                                            })
                                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //on positive click we delete the feed from selected position

                                                                    //we're gonna delete them from the db calling this method:
                                                                    removedatFeed(datposition);

                                                                    //enable ListView clicks
                                                                    listfeed.setEnabled(true);
                                                                }
                                                            })
                                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                                @Override
                                                                public void onCancel(DialogInterface dialog) {
                                                                    //enable ListView clicks
                                                                    listfeed.setEnabled(true);
                                                                }
                                                            })

                                                            .show();
                                                    return false;
                                                }
                                            }

        );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

    }

    //Adapters for the Home and Dynamic ListViews

    //method to add feeds inside the db and the dynamic ListView
    public void addFeed() {
        //dialog view
        final ViewGroup nullParent = null;

        final View dialogView = ListActivity.this.getLayoutInflater().inflate(R.layout.add_feed_layout, nullParent);

        new AlertDialog.Builder(ListActivity.this)


                .setTitle(R.string.adddialogtitle)
                .setView(dialogView)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //on click ok we get the text inputs:

                        //get the feeds urls from the input and add to array list
                        final EditText edt1 = (EditText) dialogView.findViewById(R.id.txt1);
                        feedcustom = edt1.getText().toString();
                        mUrls.add(feedcustom);

                        //get the feeds names from the input and add to array list
                        final EditText edt2 = (EditText) dialogView.findViewById(R.id.txt2);
                        feedcustom2 = edt2.getText().toString();
                        mFeeds.add(feedcustom2);

                        //and we update the ListView to add the new item
                        adapter_dynamic.notifyDataSetChanged();
                        listfeed.setAdapter(adapter_dynamic);

                        //populate the tables of the sqlite database with these values

                        //fill the urls column
                        mydb.execSQL("insert into feedslist (url) values(?);", new String[]{feedcustom});

                        //fill the names column
                        mydb.execSQL("insert into subtitleslist (name) values(?);", new String[]{feedcustom2});
                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //method to remove feeds inside the db and the dynamic ListView
    public void removedatFeed(int pos) {

        //on positive click we delete the feed from selected position

        //we're gonna delete them from the db
        //how?

        //using a cursor we select items from the two tables
        Cursor cursor = mydb.rawQuery("SELECT * FROM feedslist;", null);
        Cursor cursor2 = mydb.rawQuery("SELECT * FROM subtitleslist;", null);

        //set these values dynamically to avoid "column index out of range" errors

        String url = "";
        String name = "";

        //set url
        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                //get items at selected position
                url = mUrls.get(pos);
                cursor.moveToNext();
            }
            cursor.close();
        }

        //set feed name
        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //we get items at selected position
                name = mFeeds.get(pos);
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
        mUrls.remove(pos);

        //for feed name
        mFeeds.remove(pos);

        //and update the dynamic list
        //don't move this method above the db deletion method or you'll get javalangindexoutofboundsexception-invalid-index error
        adapter_dynamic.notifyDataSetChanged();
        adapter_dynamic.notifyDataSetInvalidated();
        listfeed.setAdapter(adapter_dynamic);
    }


    //Custom methods starts here

    //create a pending Runnable that runs in background to close the drawer smoothly
    public void closeDrawer() {
        final DrawerLayout mDrawerLayout;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                //hide the add feed menu option
                HomeUtils.hideAddFeed(addfeed);

            }
        }, 200);
    }

    //this is the method to open a new feed rss on new Thread
    public void openNewFeed(final String datfeed) {

        //close the navigation drawer
        closeDrawer();

        //show swipe refresh
        swiperefresh.setRefreshing(true);

        //detect if there's a connection issue or not: if there's a connection problem stop refreshing and show message
        if (cM.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT);
            toast.show();
            swiperefresh.setRefreshing(false);

        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DOMParser tmpDOMParser = new DOMParser();
                    fFeed = tmpDOMParser.parseXml(datfeed);
                    ListActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (fFeed != null && fFeed.getItemCount() > 0) {

                                adapter.notifyDataSetChanged();

                                //close swipe refresh
                                swiperefresh.setRefreshing(false);

                                //set feedURL calling setFeedString method, it is important if we want working swipe refresh listener
                                //setFeedString(datfeed);
                                HomeUtils.setFeedString(datfeed);

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

        //detect if there's a connection issue or not: if there's a connection problem stop refreshing and show message
        if (cM.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT);
            toast.show();
            swiperefresh.setRefreshing(false);

        } else {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DOMParser tmpDOMParser = new DOMParser();
                    fFeed = tmpDOMParser.parseXml(feedURL);
                    ListActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (fFeed != null && fFeed.getItemCount() > 0) {
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

    //this is the custom list adapter for the home ListView
    class CustomListAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        CustomListAdapter(ListActivity activity) {

            //initialize layout inflater
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //get items count
        @Override
        public int getCount() {
            return fFeed.getItemCount();
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

            //initialize feedItem
            feedItem = fFeed.getItem(pos);

            //get the feed content
            imageLink = feedItem.getImage();
            imageLink2 = feedItem.getImage2();
            feedTitle = feedItem.getTitle();
            feedDate = feedItem.getDate();

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
            lfflTitle.setText(feedTitle);

            //subtitle= publication date
            pubDate.setText(feedDate);

            //set the list items text size from preferences in SP unit
            lfflTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            pubDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);

            //initialize the ImageView
            ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);

            //if the preference is enabled remove the linear layout containing the ImageView
            if (Preferences.imagesRemoved(getBaseContext())) {

                LinearLayout linearLayout = (LinearLayout) listItem.findViewById(R.id.layout);
                linearLayout.removeAllViewsInLayout();

            }

            //else, load the image
            //if getImage() method fails (i.e when img is in content:encoded) load image2
            else if (imageLink.isEmpty()) {

                GlideUtils.loadImage(activity, imageLink2, lfflImage);

                //else use image
            } else {

                GlideUtils.loadImage(activity, imageLink, lfflImage);
            }

            return listItem;
        }
    }

    class CustomDynamicAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        //for feeds urls
        private List<String> mList;

        //for feeds names
        private List<String> mList2;

        CustomDynamicAdapter(ListActivity activity, List<String> list, List<String> list2) {

            //for urls
            mList = list;

            //for feeds names
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

            //initialize the dynamic items (feeds titles)
            TextView Title = (TextView) listItem.findViewById(R.id.title_dyn);

            //dynamically set feed's title text according to the feed data

            //set feeds titles
            Title.setText(mList2.get(position));

            return listItem;
        }
    }
}

