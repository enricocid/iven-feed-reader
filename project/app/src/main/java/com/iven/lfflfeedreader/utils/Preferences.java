package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;

import com.iven.lfflfeedreader.R;

public class Preferences {

    //This is where preferences are stored.

    //multi-preference dialog for articles text size
    public static float resolveTextSizeResId(Context context) {

        //Text size options
        float verysmall = 12;
        float small = 18;
        float medium = 24;
        float large = 30;
        float verylarge = 36;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_text_size), String.valueOf(1));
        switch (Integer.parseInt(choice)) {
            case 0:
                return verysmall;
            case 1:
            default:
                return small;
            case 2:
                return medium;
            case 3:
                return large;
            case 4:
                return verylarge;
        }
    }

    //multi-preference dialog for list items text size
    public static float resolveTextSizeListResId(Context context) {

        //Text size options
        float small_list = 10;
        float medium_list = 14;
        float large_list = 18;

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

    //multi-preference dialog for theme options
    private static int resolveTheme(Context context) {

        //Themes options

        //light theme
        int light = R.style.Theme_iven;

        //dark theme
        int dark = R.style.Theme_iven_dark;

        //darker theme
        int darker = R.style.Theme_iven_darker;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme), String.valueOf(8));
        switch (Integer.parseInt(choice)) {
            default:
            case 8:
                return light;
            case 9:
                return dark;
            case 10:
                return darker;
        }
    }

    //method to apply selected theme
    public static void applyTheme(ContextThemeWrapper contextThemeWrapper, Context context) {
        int theme = Preferences.resolveTheme(context);
        contextThemeWrapper.setTheme(theme);

    }

    //preference for the webview
    public static boolean WebViewEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("WebViewLoad", false);
    }

    //are light icons enabled?
    private static boolean LightIconsEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("lightcolored", false);
    }

    //if yes apply LightStatusBar icons
    public static void applyLightIcons(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Preferences.LightIconsEnabled(activity)) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    //Is navigation bar tint enabled?
    private static boolean NavTintEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("Navibar", false);
    }

    //if yes apply Navigation tint
    public static void applyNavTint(Activity activity, Context context, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Preferences.NavTintEnabled(activity)) {
                activity.getWindow().setNavigationBarColor(ContextCompat.getColor(context, color));
            }
        }
    }

    //is immersive mode enabled?
    private static boolean immersiveEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("Immerseme", false);

    }

    //if yes apply immersive mode
    public static void applyImmersiveMode(Activity activity) {

            if (Preferences.immersiveEnabled(activity)) {
                //immersive mode
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE

                                //Sticky flag - This is the UI you see if you use the IMMERSIVE_STICKY flag, and the user
                                //swipes to display the system bars. Semi-transparent bars temporarily appear
                                //and then hide again
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
        }

    //are images removed?
    public static boolean imagesRemoved(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("images", false);
    }
}
