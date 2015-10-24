package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import com.iven.lfflfeedreader.R;

public class Preferences {

    public static boolean WebViewEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("WebViewLoad", false);
    }

    public static boolean darkThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("Theme", false);
        }

    public static void applyTheme(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_lffl_article_dark);
        }
    }

    public static void applyTheme2(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {

            contextThemeWrapper.setTheme(R.style.Theme_lffl_dark);
        }
    }

    public static void applyTheme3(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {

            contextThemeWrapper.setTheme(R.style.Theme_lffl_info_dark);
        }

        }

    }
