package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArticleFragmentWebView extends Fragment {

	private int fPos;
	RSSFeed fFeed;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Report that this fragment would like to participate in populating the options menu
        // by receiving a call to onCreateOptionsMenu(Menu, MenuInflater) and related methods.
        setHasOptionsMenu(true);

        //Initialize the feed (i.e. get all the data)
        fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");
    }

    //create the toolbar's menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.articles_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        //apply activity's theme if dark theme is enabled
        Preferences.applyTheme(getActivity());

        //get the chosen article's text size from preferences
        float size = Preferences.resolveTextSizeResId(getContext());

       //set the view
		View view = inflater
				.inflate(R.layout.article_fragment_wb, container, false);

        //Initialize the Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        //Add the back button on toolbar
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
                activity.overridePendingTransition(0, 0);
            }
        });

        //initialize the dynamic items (the title, subtitle)
        //final because we need to access theme within the class from webview client method
		final TextView title_wb = (TextView) view.findViewById(R.id.titlewb);
		final TextView subtitle_wb = (TextView) view.findViewById(R.id.subtitlewb);

        //set the text size on articles
        title_wb.setText(fFeed.getItem(fPos).getTitle());
        subtitle_wb.setText(fFeed.getItem(fPos).getAuthor() + " - " + fFeed.getItem(fPos).getDate());

        //set the articles text size from preferences
        //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
        // size = the text size from preferences
        title_wb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle_wb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);

        //initialize the scrollview
		ScrollView scroll = (ScrollView) view.findViewById(R.id.sv_wb);

        //set smooth scroll enabled
		scroll.setSmoothScrollingEnabled(true);

        //initialize the webview
        final WebView wb = (WebView) view.findViewById(R.id.wb);

        //initialize the webview settings
		final WebSettings ws = wb.getSettings();

        //set default encoding to utf-8 to avoid malformed text
		ws.setDefaultTextEncodingName("utf-8");

        //set bg transparent because we will apply the bg using the activity's theme
		wb.setBackgroundColor(Color.TRANSPARENT);

        //control the layout of html
        //for more info http://developer.android.com/reference/android/webkit/WebSettings.LayoutAlgorithm.html
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		}

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //on click load url using the in-app webview
                        switch (item.getItemId()) {
                            case R.id.forward:
                                wb.loadUrl(fFeed.getItem(fPos).getLink());
                                return true;
                        }

                        //on click, using share intent, we open the share dialog
                        switch (item.getItemId()) {
                            case R.id.share_article:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
                                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                                return true;
                        }

                        return false;
                    }
                });

        //parse the articles text using jsoup and replace some items since this is a simple textview
        //and continue reading is not clickable
        //so we are going to replace with an empty text
		String base = fFeed.getItem(fPos).getDescription().replace("Continua a leggere...", "");

        //this is the text that will be loaded inside the html
        String baseformat = base.replace("Continue reading...", "");


        //here we handle the html where the articles will be loaded
		String html = "<!DOCTYPE html>";

        //the articles html
		html += baseformat;

        //Set different text color on light and dark themes
        //and justify the text
        //dark theme
        if (Preferences.darkThemeEnabled(getContext())) {
		html += "<body  text=\"#AEA79F\" align=\"justify\";></body>";
        } else {

            //light theme
            html += "<body  text=\"#222222\" align=\"justify\";></body>";
        }
		html += "</html>";
		wb.loadData(html, "text/html; charset=utf-8;", "utf-8");
        wb.setBackgroundColor(Color.TRANSPARENT);

        //this is a needed transformation (from float to int) to set the text size on webview
        int size_wb = Math.round(size);
        ws.setDefaultFontSize(size_wb);

        //override this method to load the url inside the in-app webview
        wb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                title_wb.setVisibility(View.GONE);
                subtitle_wb.setVisibility(View.GONE);
                return true;
            }
        });

        //enable JavaScript if the preference is enabled
        if (Preferences.JSEnabled(getContext())) {
            ws.setJavaScriptEnabled(true);
        }
		return view;
    }
}
