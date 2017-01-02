package com.iven.lfflfeedreader.infoact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
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
        Preferences.applyNavTint(this, getBaseContext(), R.color.accent_color);

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

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        public static boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
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

            //get navigation bar tint preference
            SwitchPreference navtint = (SwitchPreference) findPreference("Navibar");

            //hide this option on pre-lollipop devices
            if (Build.VERSION.SDK_INT < 21) {
                navtint.setEnabled(false);
                navtint.setSummary(getString(R.string.prelollipop));

                //show this option on >= lollipop devices
            } else {
                navtint.setEnabled(true);
            }

            //get immersive mode preference
            SwitchPreference immersivemode = (SwitchPreference) findPreference("Immerseme");

            //hide this option on pre-ics devices
            if (Build.VERSION.SDK_INT < 19) {
                immersivemode.setEnabled(false);
                immersivemode.setSummary(getString(R.string.preics));

                //show this option on >= ics devices
            } else {
                immersivemode.setEnabled(true);
            }

            //get LightStatusBar preference
            SwitchPreference LightStatusBar = (SwitchPreference) findPreference("lightcolored");

            //hide this option on pre-marshmallow devices
            if (Build.VERSION.SDK_INT < 23) {
                LightStatusBar.setEnabled(false);
                LightStatusBar.setSummary(getString(R.string.premarshmallow));

                //show this option on >= 23 devices
            } else {
                LightStatusBar.setEnabled(true);
            }

            //get the clear cache preference
            Preference preferencecache = findPreference("clearcache");

            preferencecache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    clearApplicationData();
                    return false;
                }
            });

            //initialize shared preference change listener
            //some preferences when enabled requires an app reboot
            mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                    //on theme selection restart the app
                    if (key.equals(getResources().getString(R.string.pref_theme)) | key.equals("lightcolored") | key.equals("images") | key.equals("Navibar")) {
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