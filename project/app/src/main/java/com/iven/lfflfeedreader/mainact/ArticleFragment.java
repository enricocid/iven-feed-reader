package com.iven.lfflfeedreader.mainact;

import com.bumptech.glide.Glide;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import su.whs.watl.ui.TextViewEx;

public class ArticleFragment extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.article_fragment, container, false);

        //Initialize the Toolbar
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

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

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //on click, using intents, we open the feed url using an external browser
                        switch (item.getItemId()) {
                            case R.id.forward:
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getLink()));
                                CharSequence title2 = getResources().getText(R.string.chooser_title);
                                Intent chooser = Intent.createChooser(intent, title2);
                                startActivity(chooser);
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

        //initialize the dynamic items (the title, subtitle)
		TextView title = (TextView) rootView.findViewById(R.id.title);
		TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);

        //dynamically set title and subtitle according to the feed data

        //title
        title.setText(fFeed.getItem(fPos).getTitle());

        //add date to subtitle
        subtitle.setText(fFeed.getItem(fPos).getDate());

        //initialize imageview
		ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

        //load the parsed article's image
        Glide.with(getActivity()).load(fFeed.getItem(fPos).getImage())

                    //disable cache to avoid garbage collection that may produce crashes
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

            //we can open the image on web browser on long click on the image
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getImage()));
                    CharSequence title2 = getResources().getText(R.string.chooser_title);
                    Intent chooser = Intent.createChooser(intent, title2);
                    startActivity(chooser);
                    return true;
                }
            }
            );

        //parse the articles text using jsoup and replace some items since this is a simple textview
        //and continue reading is not clickable
        //so we are going to replace with an empty text
		String base2 = Jsoup.parse(fFeed.getItem(fPos).getDescription()).text().replace("Continua a leggere...", "");

		String base2format = base2.replace("Continue reading...", "");

        String base3format = base2format.replace("Visit on site http://www.noobslab.com", "");

        //use watl lib to load justified textview
		TextViewEx articletext = (TextViewEx) rootView.findViewById(R.id.webv);

        //set the articles text
		articletext.setText(base3format);

        //set the articles text size from preferences
        //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
        // size = the text size from preferences

        articletext.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);

        return rootView;

		}
}