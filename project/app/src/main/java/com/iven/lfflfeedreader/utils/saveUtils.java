package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class saveUtils {

    //save color
    public static void saveFeedUrl(Context context, String feed) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences("feeds", Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("selectedFeed", feed)
                .apply();
    }

    //retrieve color
    public static String getFeedUrl(final Context context) {

        SharedPreferences preferenceColor = context.getSharedPreferences("feeds", Context.MODE_PRIVATE);

        return preferenceColor.getString("selectedFeed", "http://feeds.feedburner.com/androidcentral");
    }
}
