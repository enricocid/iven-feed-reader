package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;

import com.iven.lfflfeedreader.R;

public class Preferences {

    //This is where preferences are stored.

    //Text size options

    //we define some default size options
    static float verysmall = 12;
    static float small = 18;
    static float medium = 24;
    static float large = 30;
    static float verylarge = 36;
    static float small_list = 10;
    static float medium_list = 14;
    static float large_list = 18;

    //we define a method to handle the list multi-preference dialog

    //multi-preference dialog for articles text size

    public static float resolveTextSizeResId(Context context) {
        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_text_size), String.valueOf(2));
        switch (Integer.parseInt(choice)) {
            case 0:
                return verysmall;
            case 1:
                return small;
            case 2:
            default:
                return medium;
            case 3:
                return large;
            case 4:
                return verylarge;
        }
    }

    //multi-preference dialog for list items text size

    public static float resolveTextSizeListResId(Context context) {
        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_text_size_list), String.valueOf(6));
        switch (Integer.parseInt(choice)) {
            case 5:
                return small_list;
            case 6:
            default:
                return medium_list;
            case 7:
                return large_list;
        }
    }

    //preference for the webview
    public static boolean WebViewEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("WebViewLoad", false);
    }

    //preference for JavaScript
    public static boolean JSEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("JavaScriptLoad", false);
    }

    //preference for Themes

    public static boolean darkThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("Theme", false);
        }

    //Since we have various themes we need to create a preference for each theme
    //These preferences will be called when the method darkThemeEnabled will return true
    // on each activity to apply the correct theme

    //this is the preference for applying the theme on articles activity
    public static void applyTheme(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_iven_article_dark);
        }
    }

    //this is the preference for applying the theme on list activity
    public static void applyTheme2(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {

            contextThemeWrapper.setTheme(R.style.Theme_iven_dark);
        }
    }

    //this is the preference for applying the theme on info activity
    public static void applyTheme3(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {

            contextThemeWrapper.setTheme(R.style.Theme_iven_info_dark);
        }
    }

    //preference for navigation bar tint
        public static boolean navTintEnabled(Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("Navibar", false);

        }

    //preference for immersive mode
    public static boolean immersiveEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("Immerseme", false);

    }

        }
