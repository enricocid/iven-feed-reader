package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import com.iven.lfflfeedreader.R;

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
    public static String setFeedString(String feed_string) {

        return feed_string;

    }

    //this is the rate button
    public static void rate(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.iven.lfflfeedreader"));
        activity.startActivity(intent);
    }

    //this is the mail us button
    public static void mailus(Activity activity) {
        //send a mail to us
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ivenfeedreader@outlook.com"});
        intent.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.emailC)));
    }

    //this is the method to open the Google play store
    public static void shareGoogleStorePage(Activity activity) {
        //open Google Play Store page
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, ("Lffl Feed Reader"));
        activity.startActivity(Intent.createChooser(i, activity.getString(R.string.share)));
    }

}