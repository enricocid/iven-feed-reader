package com.iven.lfflfeedreader.mainact;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.utils.Preferences;

public class ArticlePage extends AppCompatActivity {

    //initialize feed infos
    String feedurl;

    //webview
    WebView wv;
    WebSettings ws;

    //ContextThemeWrapper
    ContextThemeWrapper themewrapper;

    //others
    AppCompatCheckBox js_button;
    Toolbar toolbar;
    Toast toast;
    ProgressBar progressBar;

    //create the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.wb_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actionViewItem = menu.findItem(R.id.checkbox);

        //retrieve the ActionView from menu
        final View v = MenuItemCompat.getActionView(actionViewItem);

        //find the button within actionview
        js_button = (AppCompatCheckBox) v.findViewById(R.id.js);

        js_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //enable JavaScript
                    ws.setJavaScriptEnabled(true);

                    //set Toolbar title to 'JavaScript On'
                    toolbar.setTitle("JavaScript On");
                    wv.reload();
                } else {

                    //disable JavaScript
                    ws.setJavaScriptEnabled(false);

                    //set Toolbar title to 'JavaScript Off'
                    toolbar.setTitle("JavaScript Off");
                    wv.reload();
                }
            }
        });

        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the feed's link from the intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                feedurl = extras.getString("feedselected");
            }
        }

        //apply preferences

        //apply activity's theme if dark theme is enabled
        themewrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());
        Preferences.applyTheme(themewrapper, getBaseContext());

        //set the navbar tint if the preference is enabled
        Preferences.applyNavTint(this, getBaseContext(), R.color.accent_color);

        //set LightStatusBar
        Preferences.applyLightIcons(this);

        //set the immersive mode (only for >= KitKat) if the preference is enabled
        Preferences.applyImmersiveMode(this);

        //set the view
        setContentView(R.layout.article_page_layout);

        //set the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //use a custom color
        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.wb_toolbar));

        //set status bar color on >= lollipop
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.wb_statusbar));
        }

        //set toolbar title
        getSupportActionBar().setTitle("JavaScript Off");

        //initialize the webview & its settings
        wv = (WebView) findViewById(R.id.web_view);

        ws = wv.getSettings();

        //initialize the progress bar
        progressBar = (ProgressBar) findViewById(R.id.progress);

        //set default encoding
        ws.setDefaultTextEncodingName("utf-8");

        //enum for controlling the layout of html. NORMAL means no rendering changes.
        //for more info http://developer.android.com/reference/android/webkit/WebSettings.LayoutAlgorithm.html
        ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

        //other WebView Settings

        //fit screen size
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);

        //enable pinch to zoom
        ws.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 11) {
            ws.setDisplayZoomControls(false);
        }

        //load the article's url
        wv.loadUrl(feedurl);

        //for more info about the next method:
        //http://stackoverflow.com/questions/3149216/how-to-listen-for-a-webview-finishing-loading-a-url-in-android
        wv.setWebViewClient(new WebViewClient() {

            private int running = 0; // Could be public if you want a timer to check.

            //on page started loading show circle progress
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                running = Math.max(running, 1); // First request move it to 1.

                //hide WebView
                wv.setVisibility(View.INVISIBLE);

                //show loading toast
                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.loading), Toast.LENGTH_LONG);

                toast.show();

                //show progress bar
                progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
                progressBar.setVisibility(View.VISIBLE);

            }

            //stop progress bar when the page is finished loading
            //and show the webview
            @Override
            public void onPageFinished(WebView view, String url) {
                if (--running == 0) { // just "running--;" if you add a timer.

                    //show WebView
                    wv.setVisibility(View.VISIBLE);

                    //cancel the toast if WebView has finished loading
                    toast.cancel();

                    //hide progress bar
                    progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    //(only for >= KitKat)
    //fix Immersive mode navigation becomes sticky after minimise-restore
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Preferences.applyImmersiveMode(this);
        }
    }
}

