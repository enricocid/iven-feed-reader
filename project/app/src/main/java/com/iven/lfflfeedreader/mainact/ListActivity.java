package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
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

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

	  private static final long DRAWER_CLOSE_DELAY_MS = 250;
	  private static final String NAV_ITEM_ID = "navItemId";
	  private final Handler mDrawerActionHandler = new Handler();
	  private DrawerLayout mDrawerLayout;
      private DrawerLayout mDrawerRight;
      private ActionBarDrawerToggle mDrawerToggle;
	  private int mNavItemId;

    SQLiteDatabase mydb;
	RSSFeed feed;
	ListView list;
	CustomListAdapter adapter;
    String feedURL = SplashActivity.value;
	Intent intent;
	SwipeRefreshLayout swiperefresh;
    ArrayAdapter<String> dynamic_adapter;
    ArrayList<String> dynamic_list;
    ListView listfeed;
    String feedcustom;


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
        Preferences.applyTheme2(this);

        //set the navbar tint if the preference is enabled
        if (Preferences.navTintEnabled(getBaseContext())) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.iven4));
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


        //initialize our navigation drawer's
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
        swiperefresh.setColorSchemeResources(R.color.iven2);

        //initialize the dynamic listview
        listfeed =(ListView) findViewById(R.id.listfeed);

        //initialize a sqlite database
        //here we create or open the db we are going to create later
        mydb = ListActivity.this.openOrCreateDatabase("feedslist", MODE_PRIVATE, null);

        //create a sqlite database called feedslist with a column "name" where items will be stored
        mydb.execSQL("CREATE TABLE IF NOT EXISTS feedslist (id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar);");

        //using a cursor we're going to read the database
        final Cursor cursor2 = mydb.rawQuery("SELECT * FROM feedslist;", null);

        //we create a new array list to be populated with sqlite items
        dynamic_list = new ArrayList<>();

        if (cursor2 != null && cursor2.moveToFirst()) {

            while (!cursor2.isAfterLast()) {

                //add items from column "name" to dynamic list
                dynamic_list.add(cursor2.getString(cursor2.getColumnIndex("name")));
                cursor2.moveToNext();
            }
            cursor2.close();
        }

        //initialize the dynamic list array adapter, we set a template layout and dynamic list formerly populated
        dynamic_adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, dynamic_list);

        //refresh the dynamic list
        //this is needed when we delete items to update the dynamic list
        dynamic_adapter.notifyDataSetChanged();
        listfeed.setAdapter(dynamic_adapter);

        //handle the dynamic list items click
        listfeed.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //we get the name of the item at the selected position
                String feedvalue = listfeed.getItemAtPosition(arg2).toString();

                //we send feedvalue using intents to splash activity
                final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                        ListActivity.this, SplashActivity.class));
                intent.putExtra("feedselected", feedvalue);

                //and start a new list activity with the selected feed
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //handle the dynamic list items long click
        listfeed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {
                final int deletePosition = position;

                //on long click we create a new alert dialog
                AlertDialogWrapper.Builder alert = new AlertDialogWrapper.Builder(
                        ListActivity.this);

                alert.setTitle(R.string.deletedialogtitle);
                alert.setMessage(R.string.deletedialogquestion);
                alert.setPositiveButton(R.string.deleteok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //on positive click we delete the feed from selected position
                        dynamic_list.remove(deletePosition);

                        //and we update the dynamic list
                        dynamic_adapter.notifyDataSetChanged();
                        dynamic_adapter.notifyDataSetInvalidated();
                        listfeed.setAdapter(dynamic_adapter);

                        //we also call this method to delete items from the database
                        removeRow();
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

        //attach the fab on listview to react on scroll events and
        //allow the fab to autohide when needed
        fab.attachToListView(listfeed);

        //this the method to handle fab click to open an input dialog
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(ListActivity.this)
                        .title(R.string.adddialogtitle)
                        .content(R.string.adddialogfeed)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .positiveText(android.R.string.ok)
                        .input(0, 0, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                                //on click ok we get the input
                                feedcustom = input.toString();

                                //we add this string inside the listview
                                dynamic_list.add(feedcustom);

                                //and we update the listview to add the new item
                                dynamic_adapter.notifyDataSetChanged();
                                listfeed.setAdapter(dynamic_adapter);

                                //add the new item inside the sqlite database where these
                                //dynamic items will be stored and used on app's resume to
                                //populate the listview

                                mydb.execSQL("insert into feedslist (name) values(?);", new String[]{feedcustom});
                            }
                        }).show();
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

    public void removeRow(){

        //using a cursor we select items from the sqlite database
        Cursor cursor =mydb.rawQuery("SELECT * FROM feedslist;", null);

        String id = "";
        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                //we select items by id
                id = cursor.getString(cursor.getColumnIndex("id"));
                cursor.moveToNext();
            }
            cursor.close();
        }

        //and delete the item from the database
        //this method needs to be improved since only the last item will
        //be deleted but not the inner elements
        mydb.execSQL("DELETE FROM " + "feedslist" + " WHERE " + "id"
                + "=" + id + "");
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

            //get the chosen article's text size from preferences
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
            accentColor = getResources().getColor(R.color.iven4);

        CreditsDialog.create(accentColor)
                .show(getSupportFragmentManager(), "credits");
    }

    //method to open the changelog dialog
    private void showChangelog() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0)
            accentColor = getResources().getColor(R.color.iven4);

        ChangelogDialog.create(accentColor)
                .show(getSupportFragmentManager(), "changelog");

    }
}

