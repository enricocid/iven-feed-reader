package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.view.MenuItem;

public class HomeUtils {

    public static void hideAddFeed(MenuItem menuItem) {

        //show addfeed menu item
        menuItem.setVisible(false);
    }

    public static void showAddFeed(MenuItem menuItem) {

        //show addfeed menu item
        menuItem.setVisible(true);
    }

    //method to set the feed string on feed click
    public static String setFeedString(Activity activity, String feed_string) {

        saveUtils.saveFeedUrl(activity, feed_string);
        return feed_string;

    }

}