package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import com.iven.lfflfeedreader.R;

public class Preferences {


    public static void applyTheme(ContextThemeWrapper contextThemeWrapper) {
        if (Preferences.darkThemeEnabled(contextThemeWrapper)) {
            contextThemeWrapper.setTheme(R.style.Theme_lffl_article_dark);
        }
    }

    public static boolean darkThemeEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme),
                        context.getString(R.string.pref_theme_value_light))
                .equals(context.getString(R.string.pref_theme_value_dark));
        }
    }
