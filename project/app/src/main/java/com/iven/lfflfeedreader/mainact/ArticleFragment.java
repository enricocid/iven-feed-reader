package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

public class ArticleFragment extends Fragment {

	private int fPos;
	RSSFeed fFeed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater
				.inflate(R.layout.article_fragment, container, false);			
		TextView title = (TextView) view.findViewById(R.id.title);
		WebView wb = (WebView) view.findViewById(R.id.desc);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		NestedScrollView nested = (NestedScrollView) view.findViewById(R.id.sv);
		nested.setSmoothScrollingEnabled(true);
		}
		WebSettings ws = wb.getSettings();
		ws.setDefaultTextEncodingName("utf-8");
		wb.setBackgroundColor(Color.TRANSPARENT);
        wb.setScrollContainer(false);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		}
		title.setText(fFeed.getItem(fPos).getTitle());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}

		});
		}

		String base = fFeed.getItem(fPos).getDescription()
				.toString();
		String html = "<!DOCTYPE html>";
		html += base;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		html += "<head><style type='text/css'>body{margin:1 1; color: #888888; max-width: 100%;}" +
				" img{display:inline-block; width:105%;} a:link{color:#c0392b;  text-decoration: none; text-shadow: 1px 0px #888888;" +
				"-webkit-tap-highlight-color: rgba(0, 0, 0, 0);} a:active {color:#009688;" +
				"text-decoration: none; text-shadow: 1px 0px #888888}</style></head>";
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			html += "<body  text=\"#888888\";></body>";
		}

		wb.loadData(html, "text/html; charset=utf-8;", "utf-8");

		wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            	CharSequence title2 = getResources().getText(R.string.chooser_title);
                Intent chooser = Intent.createChooser(intent, title2);
            	startActivity(chooser);
				return true;
            }
            
});
		return view;
	}
}