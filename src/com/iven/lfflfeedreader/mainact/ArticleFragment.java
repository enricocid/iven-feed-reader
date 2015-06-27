package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ScrollView;
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

		ScrollView sv = (ScrollView) view.findViewById(R.id.sv);
		sv.setVerticalFadingEdgeEnabled(true);

		// Set webview settings
		WebSettings ws = wb.getSettings();
		ws.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		
		// Set the views
				
		title.setText(fFeed.getItem(fPos).getTitle()) ;
		
		wb.loadData(fFeed.getItem(fPos).getDescription(), "text/html; charset=utf-8;", "UTF-8") ;
		wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            	CharSequence title2 = getResources().getText(R.string.chooser_title);
                Intent chooser = Intent.createChooser(intent, title2);
            	startActivity(chooser);
				return true;
            }
        @Override
        public void onLoadResource(WebView view, String url) {
            // Notice Here.
            view.clearHistory();
            super.onLoadResource(view, url);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // And Here.
            view.clearHistory();
            super.onPageFinished(view,url);
        }
});
		return view;
	}
}