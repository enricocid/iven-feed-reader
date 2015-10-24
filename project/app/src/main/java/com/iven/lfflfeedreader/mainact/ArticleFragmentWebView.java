package com.iven.lfflfeedreader.mainact;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArticleFragmentWebView extends Fragment {

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
        Preferences.applyTheme(getActivity());
		View view = inflater
				.inflate(R.layout.article_fragment_wb, container, false);
		final TextView title_wb = (TextView) view.findViewById(R.id.titlewb);
		final TextView subtitle_wb = (TextView) view.findViewById(R.id.subtitlewb);
		final WebView wb = (WebView) view.findViewById(R.id.wb);
		ScrollView scroll = (ScrollView) view.findViewById(R.id.sv_wb);
		scroll.setSmoothScrollingEnabled(true);

		final WebSettings ws = wb.getSettings();
		ws.setDefaultTextEncodingName("utf-8");
		wb.setBackgroundColor(Color.TRANSPARENT);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ws.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		}

		title_wb.setText(fFeed.getItem(fPos).getTitle());
        subtitle_wb.setText(fFeed.getItem(fPos).getAuthor() + " - " + fFeed.getItem(fPos).getDate());

        final Button share_button_wb = (Button) view.findViewById(R.id.button_share_wb);

        share_button_wb.setOnClickListener(new View.OnClickListener()

                                        {
                                            public void onClick(View v) {
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("text/plain");
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
                                                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                                            }

                                        }

        );

        final Button continue_reading_wb = (Button) view.findViewById(R.id.button_wb);

        continue_reading_wb.setOnClickListener(new View.OnClickListener()

                                            {
                                                public void onClick (View v){
                                                    wb.loadUrl(fFeed.getItem(fPos).getLink());
                                                }

                                            }

        );

		String base = fFeed.getItem(fPos).getDescription().replace("Continua a leggere...", "");
        String baseformat = base.replace("Continue reading...", "");

		String html = "<!DOCTYPE html>";
		html += baseformat;
        if (Preferences.darkThemeEnabled(getContext())) {
		html += "<body  text=\"#AEA79F\" align=\"justify\";></body>";
        } else {
            html += "<body  text=\"#222222\" align=\"justify\";></body>";
        }
		html += "</html>";
		wb.loadData(html, "text/html; charset=utf-8;", "utf-8");
        wb.setBackgroundColor(Color.TRANSPARENT);
        wb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                title_wb.setVisibility(View.GONE);
                subtitle_wb.setVisibility(View.GONE);
                share_button_wb.setVisibility(View.GONE);
                continue_reading_wb.setVisibility(View.GONE);
                return true;
            }
        });
        if (Preferences.JSEnabled(getContext())) {
            ws.setJavaScriptEnabled(true);
        }
		return view;
    }
    }