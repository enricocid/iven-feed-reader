package com.iven.lfflfeedreader.infoact;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.SplashActivity;
import com.iven.lfflfeedreader.utils.Preferences;

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

        //show this option on > lollipop devices
        } else {
            navtint.setEnabled(true);
        }

        //get immersive mode preference
        SwitchPreferenceCompat immersivemode = (SwitchPreferenceCompat) findPreference("Immerseme");

        //hide this option on pre-ics devices
        if (Build.VERSION.SDK_INT < 19){
            immersivemode.setEnabled(false);
            immersivemode.setSummary(getString(R.string.preics));

            //show this option on > ics devices
        } else {
            immersivemode.setEnabled(true);
        }

        //initialize version from BuildConfig
        String version = BuildConfig.VERSION_NAME;

        //get the Version preference
        android.support.v7.preference.Preference preferenceversion = findPreference("build_number");

        //dynamically set app's version
        preferenceversion.setSummary(version);

        //grey out version preference
        preferenceversion.setEnabled(false);

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

                //on webview on/off restart the app
                else if (key.equals("WebViewLoad")) {
                    final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
                            getActivity(), SplashActivity.class));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
}
