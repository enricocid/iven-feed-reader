package com.iven.lfflfeedreader.infoact;

import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.SplashActivity;
import com.iven.lfflfeedreader.utils.Preferences;
import com.jenzz.materialpreference.CheckBoxPreference;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;

//we use appcompat delegate to add material toolbar also on pre-lollipop devices

public class InfoActivity extends PreferenceActivity {

	private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

	private AppCompatDelegate mDelegate;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getDelegate().installViewFactory();
		getDelegate().onCreate(savedInstanceState);

		//apply activity's theme if dark theme is enabled
		Preferences.applyTheme3(this);

		super.onCreate(savedInstanceState);

        //set toolbar method
		setToolbar();
		addPreferencesFromResource(R.xml.info_pref);

        //get the context
		context = getBaseContext();

        //get the JavaScript preference
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        CheckBoxPreference js = (CheckBoxPreference) findPreference("JavaScriptLoad");

        //make the JavaScript preference visible if webview is enabled
        if (Preferences.WebViewEnabled(context)) {
            preferenceScreen.addPreference(js);
        }
        else {

            //hide the JavaScript preference if webview is disabled
            preferenceScreen.removePreference(js);
        }

        //set the navbar tint if the preference is enabled
        if (Preferences.navTintEnabled(getBaseContext())) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.iven6));
        }

        //initialize version from BuildConfig
        String version = BuildConfig.VERSION_NAME;

        //get the Version preference
        com.jenzz.materialpreference.Preference preferenceversion = (com.jenzz.materialpreference.Preference) findPreference("build_number");

        //dynamically set app's version
        preferenceversion.setSummary(version);

        //make version's preference not clickable
		preferenceversion.setSelectable(false);


        //initialize shared preference change listener
        //some preferences when enabled requires an app reboot
		mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                //on theme on/off restart the app
                if (key.equals("Theme")) {
                    Intent newIntent = new Intent(InfoActivity.this, SplashActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newIntent);
                    overridePendingTransition(0, 0);
                    finish();
                }

                //on webview on/off restart the app
                else if (key.equals("WebViewLoad")) {
				final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
						InfoActivity.this, SplashActivity.class));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				overridePendingTransition(0, 0);
                    finish();

                }

                //on navbar tint on/off restart the app
				else if (key.equals("Navibar")) {
					Intent newIntent = new Intent(InfoActivity.this, SplashActivity.class);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(newIntent);
					overridePendingTransition(0, 0);
					finish();
				}
            }
		};
    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		getDelegate().onPostCreate(savedInstanceState);

	}

	@Override
	public MenuInflater getMenuInflater() {
		return getDelegate().getMenuInflater();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
        }

        return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		getDelegate().setContentView(layoutResID);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		getDelegate().onPostResume();
	}

	@Override
	        public void onResume() {
		                super.onResume();
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
	}

	@Override
	protected void onStop() {
        super.onStop();
		getDelegate().onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getDelegate().onDestroy();
	}

	@Override
    public void onPause() {
		super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
	}

    private void setToolbar() {
		setContentView(R.layout.activity_pref);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
				ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
		getSupportActionBar().setTitle(R.string.settings);
	}

	private ActionBar getSupportActionBar() {
		return getDelegate().getSupportActionBar();
	}

	private void setSupportActionBar(@Nullable Toolbar toolbar) {
		getDelegate().setSupportActionBar(toolbar);
	}

	private AppCompatDelegate getDelegate() {
		if (mDelegate == null) {
			mDelegate = AppCompatDelegate.create(this, null);
		}
		return mDelegate;
	}

}
