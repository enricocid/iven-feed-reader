package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArticleFragmentWebView extends Fragment {

	private int fPos;
	RSSFeed fFeed;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Initialize the feed (i.e. get all the data)
        fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        //apply activity's theme if dark theme is enabled
        Preferences.applyTheme(getActivity());

       //set the view
		View view = inflater
				.inflate(R.layout.article_fragment_wb, container, false);

        //initialize the webview
        final WebView wb = (WebView) view.findViewById(R.id.wb);

        //Article title and subtitle
        //title
        final TextView title_wb = (TextView) view.findViewById(R.id.titlewb);

        //subtitle
        final TextView subtitle_wb = (TextView) view.findViewById(R.id.subtitlewb);

        //Action Buttons
        //text view under read more button
        final TextView continue_text = (TextView) view.findViewById(R.id.txt_continue);

        //text view under share button
        TextView share_text = (TextView) view.findViewById(R.id.txt_share);

        //text view under back button
        TextView back_text = (TextView) view.findViewById(R.id.txt_back);

        //read more button
        final ImageButton button_continue_reading = (ImageButton) view.findViewById(R.id.button_continue);

        //this is the method to handle the continue reading button click
        //on click remove title, subtitle, article view and continue reading from the view

        //initialize the article view linear layout
        final LinearLayout article_linearLayout_wb = (LinearLayout) view.findViewById(R.id.article_wb_linearlayout);

        //the view below title/subtitle
        final View article_view = view.findViewById(R.id.article_wb_view);

        //percentRelativeLayout containing action buttons
        final PercentRelativeLayout article_percentlayout_wb = (PercentRelativeLayout) view.findViewById(R.id.action_buttons);

        View.OnClickListener listener_forward = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wb.loadUrl(fFeed.getItem(fPos).getLink());
                article_percentlayout_wb.removeView(button_continue_reading);
                article_percentlayout_wb.removeView(continue_text);
                article_linearLayout_wb.removeView(title_wb);
                article_linearLayout_wb.removeView(subtitle_wb);
                article_linearLayout_wb.removeView(article_view);
            }
        };

        //set continue reading/share TextViews listeners
        button_continue_reading.setOnClickListener(listener_forward);

        //share button
        ImageButton button_share = (ImageButton) view.findViewById(R.id.button_share);

        //this is the method to handle the share button click
        View.OnClickListener listener_share = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        };

        //set the share on click listener
        button_share.setOnClickListener(listener_share);

        //back button
        ImageButton button_back = (ImageButton) view.findViewById(R.id.button_back);;

        //this is the click listener to provide back navigation
        View.OnClickListener listener_back = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        };

        //set back button on click listener
        button_back.setOnClickListener(listener_back);

        //dynamically set title and subtitle according to the feed data
        //set title of the article
        title_wb.setText(fFeed.getItem(fPos).getTitle());

        //set the date of the article to subtitle
        subtitle_wb.setText(fFeed.getItem(fPos).getDate());

        //set the article texts size from preferences
        //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
        // size = the text size from preferences

        //get the chosen article's text size from preferences
        float size = Preferences.resolveTextSizeResId(getContext());

        //this is a needed transformation (from float to int) to set the text size on webview
        final int size_wb = Math.round(size);

        title_wb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle_wb.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);
        continue_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size- 2);
        share_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);
        back_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);

        //WebView related things
        //initialize the webview settings

        //set smooth scroll enabled
        //initialize the scrollview
        final ScrollView scroll = (ScrollView) view.findViewById(R.id.sv_wb);

        scroll.setSmoothScrollingEnabled(true);

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
		html += "<body  text=\"#b1b1b1\" align=\"justify\";></body>";
        } else {

            //light theme
            html += "<body  text=\"#4e4e4e\" align=\"justify\";></body>";
        }
		html += "</html>";
		wb.loadData(html, "text/html; charset=utf-8;", "utf-8");
        wb.setBackgroundColor(Color.TRANSPARENT);

        ws.setDefaultFontSize(size_wb);

        //override this method to load the url inside the in-app webview
        wb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                return true;
            }
        });

        //enable JavaScript if the preference is enabled
        if (Preferences.JSEnabled(getContext())) {
            ws.setJavaScriptEnabled(true);
        }

        return view;
    }

    //share method
    public void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share) + " '" + fFeed.getItem(fPos).getTitle() + "'"));

    }

    //back navigation method
    public void goBack() {

        //Cast getActivity() to AppCompatActivity to have access to support appcompat methods (onBackPressed();)
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.onBackPressed();
    }
}
