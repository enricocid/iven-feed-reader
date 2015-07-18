package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
		FloatingActionButton fab = (FloatingActionButton)  view.findViewById(R.id.fab);
		
		WebSettings ws = wb.getSettings();
		ws.setDefaultTextEncodingName("utf-8");
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		}
		title.setText(fFeed.getItem(fPos).getTitle());

		
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
				getActivity().onBackPressed();
            }

		});

		wb.loadData(fFeed.getItem(fPos).getDescription(), "text/html; charset=utf-8;", "utf-8");
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