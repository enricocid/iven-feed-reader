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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

        //Initialize the feed (i.e. get all the data
        fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");

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

        //initialize the dynamic items (the title, subtitle)
		TextView title = (TextView) rootView.findViewById(R.id.title);
		TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);

        //dynamically set title and subtitle according to the feed data

        //title
        title.setText(fFeed.getItem(fPos).getTitle());

        //add author + date to subtitle
        subtitle.setText(fFeed.getItem(fPos).getAuthor() + " - " + fFeed.getItem(fPos).getDate());

        //initialize imageview
		ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

        //use glide to load the image into the imageview (imageView)
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

        //continue reading button
			ImageButton continue_reading = (ImageButton) rootView.findViewById(R.id.button);

        //on click, using intents, we open the feed url using an external browser
			continue_reading.setOnClickListener(new View.OnClickListener()

                                                {
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getLink()));
                                                        CharSequence title2 = getResources().getText(R.string.chooser_title);
                                                        Intent chooser = Intent.createChooser(intent, title2);
                                                        startActivity(chooser);
                                                    }

                                                }

            );

        //share button

        ImageButton share_button = (ImageButton) rootView.findViewById(R.id.button_share);

        //on click, using share intent, we open the share dialog

        share_button.setOnClickListener(new View.OnClickListener()

                                        {
                                            public void onClick(View v) {
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("text/plain");
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
                                                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
                                            }

                                        }

        );

		return rootView;

		}
}