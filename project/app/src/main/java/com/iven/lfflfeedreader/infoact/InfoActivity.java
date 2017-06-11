package com.iven.lfflfeedreader.infoact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.SplashActivity;
import com.iven.lfflfeedreader.utils.Preferences;

import java.io.File;

public class InfoActivity extends AppCompatActivity {

    //ContextThemeWrapper
    ContextThemeWrapper themewrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //apply preferences

        //apply activity's theme if dark theme is enabled
        themewrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());
        Preferences.applyTheme(themewrapper, getBaseContext());

        //set the navbar tint if the preference is enabled
        Preferences.applyNavTint(this);

        //set LightStatusBar
        Preferences.applyLightIcons(this);

        //set the view
        setContentView(R.layout.preference_activity);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, 0);
            }
        });


        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        SwitchPreference LightStatusBar;
        Preference preferenceCache;
        ListPreference timePreference;
        RingtonePreference soundPreference;

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        public static boolean deleteDir(File dir) {

            final String[] children = dir.list();
            boolean success;

            if (dir.isDirectory()) {

                for (String s : children) {
                    success = deleteDir(new File(dir, s));

                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.info_pref);

            //get preference screen
            final PreferenceScreen screen = getPreferenceScreen();

            //get LightStatusBar preference
            LightStatusBar = (SwitchPreference) findPreference("lightcolored");

            //hide this option on pre-marshmallow devices
            if (Build.VERSION.SDK_INT < 23) {
                LightStatusBar.setEnabled(false);
                LightStatusBar.setSummary(getString(R.string.premarshmallow));

                //show this option on >= 23 devices
            } else {
                LightStatusBar.setEnabled(true);
            }

            //get the clear cache preference
            preferenceCache = findPreference("clearcache");

            preferenceCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    clearApplicationData();
                    return false;
                }
            });

            //get sound and time preference
            timePreference = (ListPreference) findPreference("pref_time");
            soundPreference = (RingtonePreference) findPreference("audio");

            if (Preferences.notificationsEnabled(getActivity())) {

                screen.addPreference(timePreference);
                screen.addPreference(soundPreference);

            } else {
                screen.removePreference(timePreference);
                screen.removePreference(soundPreference);

            }

            //initialize shared preference change listener
            //some preferences when enabled requires an app reboot
            mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                    //on theme selection restart the app
                    if (key.equals(getResources().getString(R.string.pref_theme)) | key.equals("lightcolored") | key.equals("images") | key.equals("Navibar") | (key.equals("notifications"))) {
                        restartApp();
                    }
                }
            };
        }

        //register preferences changes
        @Override
        public void onResume() {
            super.onResume();

            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
        }

        //unregister preferences changes
        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
            super.onPause();
        }

        //this is the method to delete the app's cache
        public void clearApplicationData() {
            File cache = getActivity().getCacheDir();
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

        //method to restart the app and apply the changes
        private void restartApp() {
            Intent newIntent = new Intent(getActivity(), SplashActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newIntent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }
}