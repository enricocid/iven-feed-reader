package com.iven.lfflfeedreader.infoact;

import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.SplashActivity;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class InfoActivity extends PreferenceActivity {

	private SharedPreferences.OnSharedPreferenceChangeListener mListenerTheme;

	private AppCompatDelegate mDelegate;

	Preference iconos, jso, glide, materialicons, prog, tushpal, compat, stacking, path, materialpreferences, matpal, materialdialogs, trung, textjust, responsive;
	
	Context context;

	String versionName = BuildConfig.VERSION_NAME;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getDelegate().installViewFactory();
		getDelegate().onCreate(savedInstanceState);
		Preferences.applyTheme3(this);
		super.onCreate(savedInstanceState);

		setToolbar();
		addPreferencesFromResource(R.xml.info_pref);

		context = getBaseContext();

		mListenerTheme = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String id) {
				if (android.os.Build.VERSION.SDK_INT <= 11) {
					Intent newIntent = new Intent(InfoActivity.this,SplashActivity.class);
					newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					finish();
					startActivity(newIntent);
					overridePendingTransition(0, 0);
				}
				else {

				final Intent intent = IntentCompat.makeMainActivity(new ComponentName(
						InfoActivity.this, SplashActivity.class));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
					finish();
					startActivity(intent);
				overridePendingTransition(0, 0);

				}
			}
		};

		jso = this.findPreference("js");
		jso.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jhy/jsoup")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
        glide = this.findPreference("glideimagemanager");
		glide.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/bumptech/glide")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
        iconos = this.findPreference("robot");
		iconos.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/design/")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		materialicons = this.findPreference("mater");
		materialicons.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse("http://materialdesignicons.com/")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		prog = this.findPreference("progressish");
		prog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pnikosis/materialish-progress")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		tushpal = this.findPreference("pal");
		tushpal.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/118084639598782231216/posts")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		compat = this.findPreference("support");
		compat.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/tools/support-library/index.html")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		stacking = this.findPreference("stackover");
		stacking.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://stackoverflow.com/")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		path = this.findPreference("codepathguides");
		path.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://guides.codepath.com/android")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		materialpreferences = this.findPreference("matpref");
		materialpreferences.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jenzz/Android-MaterialPreference")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		matpal = this.findPreference("palettes");
		matpal.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.materialpalette.com")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});
		
		materialdialogs = this.findPreference("matdialogs");
		materialdialogs.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/afollestad/material-dialogs")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));
				}
				return false;
				}
			});

		trung = this.findPreference("duy");
		trung.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hidroh.com/2015/02/16/support-multiple-themes-android-app/")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));

				}
				return false;
			}
		});

		textjust = this.findPreference("whs");
		textjust.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/suwhs/wATL")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));

				}
				return false;
			}
		});

		responsive = this.findPreference("respotool");
		responsive.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://medium.com/@lucasurbas/making-android-toolbar-responsive-2627d4e07129")));
				} catch (ActivityNotFoundException anfe)
				{
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT);
					toast.show();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=browsers")));

				}
				return false;
			}
		});
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
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerTheme);
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
		                getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerTheme);
	}
	private void setToolbar() {
		setContentView(R.layout.activity_pref);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setSubtitle(versionName);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
				ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
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
