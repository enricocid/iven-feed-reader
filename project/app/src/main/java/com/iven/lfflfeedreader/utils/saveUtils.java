package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class saveUtils {

    //save url
    static void saveFeedUrl(Context context, String feed) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences("feeds", Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("selectedFeed", feed)
                .apply();
    }

    //retrieve url
    public static String getFeedUrl(final Context context) {

        SharedPreferences preferenceColor = context.getSharedPreferences("feeds", Context.MODE_PRIVATE);

        return preferenceColor.getString("selectedFeed", "http://feeds.feedburner.com/androidcentral");
    }

    //save last date
    static void saveLastDate(Context context, String listCount) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences("date", Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("lastDate", listCount)
                .apply();
    }

    //retrieve last date
    static String getLastDate(final Context context) {

        SharedPreferences preferenceColor = context.getSharedPreferences("date", Context.MODE_PRIVATE);

        return preferenceColor.getString("lastDate", "0");
    }
}
