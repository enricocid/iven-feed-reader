package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.View;

import com.iven.lfflfeedreader.R;

public class Preferences {

    //This is where preferences are stored.

    public static int getThemeAccentColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorAccent, value, true);
        return value.data;
    }

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

    //multi-preference dialog for notification options
    static int resolveTime(Context context) {

        //Text size options
        int minimum = 15;
        int normal = 30;
        int onemin = 60;
        int fivemin = 300;
        int tenmin = 600;
        int twentymin = 1200;
        int thirtymin = 1800;
        int onehour = 3600;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_time), String.valueOf(47));
        switch (Integer.parseInt(choice)) {
            case 46:
                return minimum;
            default:
            case 47:
                return normal;
            case 48:
                return onemin;
            case 49:
                return fivemin;
            case 50:
                return tenmin;
            case 51:
                return twentymin;
            case 52:
                return thirtymin;
            case 53:
                return onehour;
        }
    }

    //multi-preference dialog for list items text size
    public static int resolveTextSizeListResId(Context context) {

        //Text size options
        int small_list = 10;
        int medium_list = 14;
        int large_list = 18;

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
        int light = R.style.Base_Theme;

        //dark theme
        int dark = R.style.Base_Theme_Dark;

        //other themes
        int red = R.style.Base_Theme_Red;
        int nightRed = R.style.Base_Theme_Dark_Red;
        int pink = R.style.Base_Theme_Pink;
        int nightPink = R.style.Base_Theme_Dark_Pink;
        int purple = R.style.Base_Theme_Purple;
        int nightPurple = R.style.Base_Theme_Dark_Purple;
        int deepPurple = R.style.Base_Theme_DeepPurple;
        int nightDeepPurple = R.style.Base_Theme_Dark_DeepPurple;
        int indigo = R.style.Base_Theme_Indigo;
        int nightIndigo = R.style.Base_Theme_Dark_Indigo;
        int blue = R.style.Base_Theme_Blue;
        int nightBlue = R.style.Base_Theme_Dark_Blue;
        int lightBlue = R.style.Base_Theme_LightBlue;
        int nightLightBlue = R.style.Base_Theme_Dark_LightBlue;
        int cyan = R.style.Base_Theme_Cyan;
        int nightCyan = R.style.Base_Theme_Dark_Cyan;
        int teal = R.style.Base_Theme_Teal;
        int nightTeal = R.style.Base_Theme_Dark_Teal;
        int green = R.style.Base_Theme_Green;
        int nightGreen = R.style.Base_Theme_Dark_Green;
        int lightGreen = R.style.Base_Theme_LightGreen;
        int nightLightGreen = R.style.Base_Theme_Dark_LightGreen;
        int lime = R.style.Base_Theme_Lime;
        int nightLime = R.style.Base_Theme_Dark_Lime;
        int yellow = R.style.Base_Theme_Yellow;
        int nightYellow = R.style.Base_Theme_Dark_Yellow;
        int amber = R.style.Base_Theme_Amber;
        int nightAmber = R.style.Base_Theme_Dark_Amber;
        int orange = R.style.Base_Theme_Orange;
        int nightOrange = R.style.Base_Theme_Dark_Orange;
        int deepOrange = R.style.Base_Theme_DeepOrange;
        int nightDeepOrange = R.style.Base_Theme_Dark_DeepOrange;
        int brown = R.style.Base_Theme_Brown;
        int nightBrown = R.style.Base_Theme_Dark_Brown;
        int blueGrey = R.style.Base_Theme_BlueGrey;
        int nightBlueGrey = R.style.Base_Theme_Dark_BlueGrey;


        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme), String.valueOf(8));
        switch (Integer.parseInt(choice)) {
            default:
            case 8:
                return light;
            case 9:
                return dark;

            case 10:
                return red;

            case 11:
                return nightRed;
            case 12:
                return pink;
            case 13:
                return nightPink;
            case 14:
                return purple;
            case 15:
                return nightPurple;
            case 16:
                return deepPurple;
            case 17:
                return nightDeepPurple;
            case 18:
                return indigo;
            case 19:
                return nightIndigo;
            case 20:
                return blue;
            case 21:
                return nightBlue;
            case 22:
                return lightBlue;
            case 23:
                return nightLightBlue;
            case 24:
                return cyan;
            case 25:
                return nightCyan;
            case 26:
                return teal;
            case 27:
                return nightTeal;
            case 28:
                return green;
            case 29:
                return nightGreen;
            case 30:
                return lightGreen;
            case 31:
                return nightLightGreen;
            case 32:
                return lime;
            case 33:
                return nightLime;
            case 34:
                return yellow;
            case 35:
                return nightYellow;
            case 36:
                return amber;
            case 37:
                return nightAmber;
            case 38:
                return orange;
            case 39:
                return nightOrange;
            case 40:
                return deepOrange;
            case 41:
                return nightDeepOrange;
            case 42:
                return brown;
            case 43:
                return nightBrown;
            case 44:
                return blueGrey;
            case 45:
                return nightBlueGrey;

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
    public static void applyNavTint(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Preferences.NavTintEnabled(activity)) {
                activity.getWindow().setNavigationBarColor(getThemeAccentColor(activity));
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

    //are notifications enabled?
    public static boolean notificationsEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("notifications", false);
    }
}
