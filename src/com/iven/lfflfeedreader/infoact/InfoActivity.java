package com.iven.lfflfeedreader.infoact;


import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.iven.lfflfeedreader.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class InfoActivity extends PreferenceActivity{

	Preference facebook, twitter, google, youtube, mosca1, iconos, glic, gstud, simo,jso, laz, materialicons, prog, tushpal, compat, swipy, stacking, path;
	
	Context context;
	Intent intent;
	Toolbar mToolbar;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        LinearLayout content = (LinearLayout) root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_pref, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        mToolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar3);
    
		addPreferencesFromResource(R.xml.info_pref);
    
		mToolbar.setTitle(R.string.informations);
		mToolbar.setNavigationIcon(R.drawable.ic_back);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setSupportActionBar(mToolbar);
               
		context = getBaseContext();
				
		facebook = (Preference) this.findPreference("facebook");
		facebook.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/LinuX-Freedom-for-Live/290943601160"));
			if(intent != null) {
			startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
			}
	});

		twitter = (Preference) this.findPreference("twitter");
		twitter.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/linuxfreedom"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		google = (Preference) this.findPreference("google");
		google.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+lffl/posts"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
		jso = (Preference) this.findPreference("js");
		jso.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jhy/jsoup"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
		mosca1 = (Preference) this.findPreference("ferramosca");
		mosca1.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/100021192050184001006/posts"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		simo = (Preference) this.findPreference("simonedev");
		simo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://simonedev.blogspot.it/"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		youtube = (Preference) this.findPreference("videos");
		youtube.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/linuxfreedomforlive"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
laz = (Preference) this.findPreference("thest");
		
		laz.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/thest1/LazyList"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
iconos = (Preference) this.findPreference("robot");
		
		iconos.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/design/"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
glic = (Preference) this.findPreference("robotlicense");
		
		glic.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://source.android.com/license.html"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
gstud = (Preference) this.findPreference("studio");
		
		gstud.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://code.google.com/p/android-ui-utils/"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		materialicons = (Preference) this.findPreference("mater");
		materialicons.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://materialdesignicons.com/"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
		prog = (Preference) this.findPreference("progressish");
		prog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pnikosis/materialish-progress"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show(); 
			}
			return false;
				}
			});
		
		tushpal = (Preference) this.findPreference("pal");
		tushpal.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/118084639598782231216/posts"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		compat = (Preference) this.findPreference("support");
		compat.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/tools/support-library/index.html"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		swipy = (Preference) this.findPreference("pullrefresh");
		swipy.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/baoyongzhang/android-PullRefreshLayout"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		stacking = (Preference) this.findPreference("stackover");
		stacking.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stackoverflow.com/"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
		
		path = (Preference) this.findPreference("codepathguides");
		path.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick (Preference preference) {
			
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://guides.codepath.com/android"));
			if(intent != null) {
				startActivity(intent);
			} else {
				Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition), R.string.error, Snackbar.LENGTH_LONG);
	            View view = snack.getView();
	            view.setBackgroundColor(Color.RED); 
	            snack.show();
			}
			return false;
				}
			});
    }
	
		private void setSupportActionBar(Toolbar toolbar) {
		// TODO Auto-generated method stub
		
	}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {

			case android.R.id.home:

				finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
}
