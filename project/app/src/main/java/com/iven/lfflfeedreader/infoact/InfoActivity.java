package com.iven.lfflfeedreader.infoact;

import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class InfoActivity extends PreferenceActivity{

	private AppCompatDelegate mDelegate;

	Preference facebook, twitter, google, youtube, mosca1, iconos, glic, gstud, simo,jso, laz, materialicons, prog, tushpal, compat, stacking, path, materialpreferences, matpal, materialdialogs, geecko86, trung;
	
	Context context;

	String versionName = BuildConfig.VERSION_NAME;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		getDelegate().installViewFactory();
		getDelegate().onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setToolbar();
		addPreferencesFromResource(R.xml.info_pref);

		context = getBaseContext();
				
		facebook = this.findPreference("facebook");
		facebook.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/LinuX-Freedom-for-Live/290943601160")));
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

		twitter = this.findPreference("twitter");
		twitter.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/linuxfreedom")));
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
		
		google = this.findPreference("google");
		google.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+lffl/posts")));
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
		
		mosca1 = this.findPreference("ferramosca");
		mosca1.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/100021192050184001006/posts")));
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
		
		simo = this.findPreference("simonedev");
		simo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://simonedev.blogspot.it/")));
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
		
		youtube = this.findPreference("videos");
		youtube.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/linuxfreedomforlive")));
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
		
        laz = this.findPreference("thest");
		laz.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/thest1/LazyList")));
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
		
        glic = this.findPreference("robotlicense");
		glic.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://source.android.com/license.html")));
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
		
        gstud = this.findPreference("studio");
		gstud.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://code.google.com/p/android-ui-utils/")));
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

		geecko86 = this.findPreference("geecko");
		geecko86.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {

				try
				{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/geecko86")));
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
	protected void onStop() {
		super.onStop();
		getDelegate().onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getDelegate().onDestroy();
	}

	private void setToolbar() {
		setContentView(R.layout.activity_pref);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
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
