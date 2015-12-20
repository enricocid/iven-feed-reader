package com.iven.lfflfeedreader.infoact;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.widget.Toast;

import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.SplashActivity;
import com.iven.lfflfeedreader.utils.Preferences;

import java.io.File;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the JavaScript preference
        android.support.v7.preference.CheckBoxPreference js = (android.support.v7.preference.CheckBoxPreference) findPreference("JavaScriptLoad");

        //make the JavaScript preference visible if webview is enabled
        if (Preferences.WebViewEnabled(getContext())) {
            js.setEnabled(true);

        }
        else {
            //hide the JavaScript preference if webview is disabled
            js.setEnabled(false);
        }

        //get navigation bar tint preference
        SwitchPreferenceCompat navtint = (SwitchPreferenceCompat) findPreference("Navibar");

        //hide this option on pre-lollipop devices
        if (Build.VERSION.SDK_INT < 21){
            navtint.setEnabled(false);
            navtint.setSummary(getString(R.string.prelollipop));

        //show this option on >= lollipop devices
        } else {
            navtint.setEnabled(true);
        }

        //get immersive mode preference
        SwitchPreferenceCompat immersivemode = (SwitchPreferenceCompat) findPreference("Immerseme");

        //hide this option on pre-ics devices
        if (Build.VERSION.SDK_INT < 19){
            immersivemode.setEnabled(false);
            immersivemode.setSummary(getString(R.string.preics));

            //show this option on >= ics devices
        } else {
            immersivemode.setEnabled(true);
        }

        //get LightStatusBar preference
        SwitchPreferenceCompat LightStatusBar = (SwitchPreferenceCompat) findPreference("lightcolored");

        //hide this option on pre-marshmallow devices
        if (Build.VERSION.SDK_INT < 23){
            LightStatusBar.setEnabled(false);
            LightStatusBar.setSummary(getString(R.string.premarshmallow));

            //show this option on >= 23 devices
        } else {
            LightStatusBar.setEnabled(true);
        }

        //get the clear cache preference
        android.support.v7.preference.Preference preferencecache = findPreference("clearcache");

        preferencecache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference preference) {

                clearApplicationData();
                return false;
            }
        });

        //initialize version from BuildConfig
        String version = BuildConfig.VERSION_NAME;

        //get the Version preference
        android.support.v7.preference.Preference preferenceversion = findPreference("build_number");

        //dynamically set app's version
        preferenceversion.setSummary(version);

        //grey out version preference
        preferenceversion.setEnabled(false);

        //get the changelog preference
        android.support.v7.preference.Preference preferencechangelog = findPreference("changelog");

        preferencechangelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference preference) {

                showChangelog();
                return false;
            }
        });
        //get the credits preference
        android.support.v7.preference.Preference preferencecredits = findPreference("credits");

        preferencecredits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference preference) {

                showInfo();
                return false;
            }
        });

        //get the source code preference
        android.support.v7.preference.Preference preferencesource = findPreference("source");

        preferencesource.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick (Preference preference) {

                showSource();
                return false;
            }
        });

        //initialize shared preference change listener
        //some preferences when enabled requires an app reboot
        mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                //on theme on/off restart the app
                if (key.equals("Theme")) {
                    Intent newIntent = new Intent(getActivity(), SplashActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();
                }

                //on LightStatusBar on/off restart the app
                else if (key.equals("lightcolored")) {
                    Intent newIntent = new Intent(getActivity(), SplashActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();
                }

                //on webview on/off restart the app
                else if (key.equals("WebViewLoad")) {
                    final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                            getActivity(), SplashActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();

                }

                //on disable images on/off restart the app
                else if (key.equals("images")) {
                    					Intent newIntent = new Intent(getActivity(), SplashActivity.class);
                    					newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    					startActivity(newIntent);
                                        getActivity().overridePendingTransition(0, 0);
                                        getActivity().finish();
                }

                //on navbar tint on/off restart the app
                else if (key.equals("Navibar")) {
                    Intent newIntent = new Intent(getActivity(), SplashActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();
                }
            }
        };
    }

    //add preferences from xml
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.info_pref);
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

    //method to open the credits dialog
    private void showInfo() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0)
            accentColor = ContextCompat.getColor(getContext(), R.color.accent_color);

        CreditsDialog.create(accentColor)
                .show(getActivity().getSupportFragmentManager(), "credits");
    }

    //method to open the changelog dialog
    private void showChangelog() {
        int accentColor = ThemeSingleton.get().widgetColor;
        if (accentColor == 0)
            accentColor = ContextCompat.getColor(getContext(), R.color.accent_color);

        ChangelogDialog.create(accentColor)
                .show(getActivity().getSupportFragmentManager(), "changelog");

    }

    //method to open the git page
    private void showSource() {

        try
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/enricocid/iven-feed-reader")));

            //if no browser is found open Google Play Store
        } catch (ActivityNotFoundException anfe)
        {

            Toast toast = Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT);
            toast.show();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
        }
    }
}
