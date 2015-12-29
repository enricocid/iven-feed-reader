package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.utils.Preferences;
import com.iven.lfflfeedreader.utils.ScrollAwareFABBehavior;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ArticlePage extends AppCompatActivity implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    //initialize feed infos
    String feedurl;

    //webview
    WebView wv;

    //ContextThemeWrapper
    ContextThemeWrapper themewrapper;

    //others
    SwipeRefreshLayout swipeRefreshLayout;
    SwitchCompat js_button;
    FloatingActionButton fab_back;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //get the feed's link from the intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
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

        //back button
        fab_back = (FloatingActionButton) findViewById(R.id.back);

        //this is the click listener to provide back navigation
        View.OnClickListener listener_back = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

        //set back button on click listener
        fab_back.setOnClickListener(listener_back);

        //set fab's behavior
        //initialize coordinator layout
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab_back.getLayoutParams();
        p.setBehavior(new ScrollAwareFABBehavior());
        fab_back.setLayoutParams(p);

        //initialize SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_ap);

        //set on refresh listener
        swipeRefreshLayout.setOnRefreshListener(this);

        //set the default color of the arrow
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_color);

        //initialize the webview & its settings
        wv = (WebView) findViewById(R.id.web_view);

        final WebSettings webview_settings = wv.getSettings();

        //the loading text
        final TextView text_load = (TextView) findViewById(R.id.loading_text);

        //switch button to enable JavaScript
        js_button = (SwitchCompat) findViewById(R.id.js);

        js_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.reload();
                } else {
                    wv.getSettings().setJavaScriptEnabled(false);
                    wv.reload();
                }
            }
        });

        //set default encoding
        webview_settings.setDefaultTextEncodingName("utf-8");

        //enum for controlling the layout of html. NORMAL means no rendering changes.
        //for more info http://developer.android.com/reference/android/webkit/WebSettings.LayoutAlgorithm.html
        webview_settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

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

                //and hide JS, fab buttons and the web icon
                js_button.setVisibility(View.INVISIBLE);
                fab_back.hide();
                text_load.setVisibility(View.VISIBLE);

                //hide WebView
                wv.setVisibility(View.INVISIBLE);

                //show progress circle
                swipeRefreshLayout.setRefreshing(true);
            }

            //stop progress circle when the page is finished loading
            //and show the webview and other things
            @Override
            public void onPageFinished(WebView view, String url) {
                if (--running == 0) { // just "running--;" if you add a timer.

                    //show WebView
                    wv.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    js_button.setVisibility(View.VISIBLE);
                    text_load.setVisibility(View.INVISIBLE);
                    fab_back.show();
                }
            }
        });
    }

    //refresh the page using swipe refresh layout
    public void onRefresh() {

        //reload the page
        wv.reload();

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

