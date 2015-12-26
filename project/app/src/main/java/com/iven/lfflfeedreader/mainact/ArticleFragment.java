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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;

public class ArticleFragment extends Fragment {

    //position
	private int fPos;

    //feed
	RSSFeed fFeed;
    String base2;
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

                //set the view
                ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.article_fragment, container, false);

        //Article title and subtitle
        //title
        TextView title = (TextView) rootView.findViewById(R.id.title);

        //subtitle
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);

        //Action Buttons

        //read more button
        ImageButton button_continue_reading = (ImageButton) rootView.findViewById(R.id.button_continue);

        //this is the method to handle the continue reading button click
        View.OnClickListener listener_forward = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReading();
            }
        };

        //set Read more listener
        button_continue_reading.setOnClickListener(listener_forward);

        //share button
        ImageButton button_share = (ImageButton) rootView.findViewById(R.id.button_share);

        //this is the method to handle the share button click
        View.OnClickListener listener_share = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        };

        //set Share listener
        button_share.setOnClickListener(listener_share);

        //back button
        ImageButton button_back = (ImageButton) rootView.findViewById(R.id.button_back);

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
        title.setText(fFeed.getItem(fPos).getTitle());

        //set the date of the article to subtitle
        subtitle.setText(fFeed.getItem(fPos).getDate());

        //set the articles text size from preferences
        //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
        // size = the text size from preferences

        //get the chosen article's text size from preferences
        float size = Preferences.resolveTextSizeResId(getContext());

        //set it
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);

        //if the preference is enabled remove the imageview from the linear layout

        //imageview
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

        //initialize the article view linear layout
        LinearLayout article_default_linearLayout = (LinearLayout) rootView.findViewById(R.id.article_linearlayout);

        if (Preferences.imagesRemoved(getContext())) {

            article_default_linearLayout.removeView(imageView);

        }

        //else, load the image
        //if getImage() method fails (i.e when img is in content:encoded) load image2
        else if (fFeed.getItem(fPos).getImage().isEmpty()) {

            //use glide to load the image into the imageview (imageView)
            Glide.with(getActivity()).load(fFeed.getItem(fPos).getImage2())

                    //disable cache to avoid garbage collection that may produce crashes
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

            //else use image
        } else {

            //load the parsed article's image using glide
            Glide.with(getActivity()).load(fFeed.getItem(fPos).getImage())

                    //disable cache to avoid garbage collection that may produce crashes
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }

            //we can open the image on web browser on long click on the image
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                                 public boolean onLongClick(View v) {

                                                     final Intent intent;

                                                     //use glide to load the image into the imageview (imageView)
                                                     //if getImage() method fails (i.e when img is in content:encoded) load image2
                                                     if (fFeed.getItem(fPos).getImage().isEmpty()) {
                                                         intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getImage2()));

                                                     }

                                                     //else use image
                                                     else {
                                                         intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getImage()));

                                                     }
                                                     CharSequence title2 = getResources().getText(R.string.chooser_title);
                                                     Intent chooser = Intent.createChooser(intent, title2);
                                                     startActivity(chooser);
                                                     return true;
                                                 }
                                             }
            );

        //Set the article's content

        //we use complete description by default, but sometimes the method returns null
        //so, if getCompleteDescription is null use description
        if(fFeed.getItem(fPos).getCompleteDescription().contains("no desc")) {
            base2 = Jsoup.parse(fFeed.getItem(fPos).getDescription()).text().replace("Continua a leggere...", "");

        // else, use complete description
        } else {
            base2 = Jsoup.parse(fFeed.getItem(fPos).getCompleteDescription()).text().replace("Continua a leggere...", "");

        }

        //replace some items since this is a simple textview
        //and continue reading hyperlinks are not clickable
        //so we are going to replace them with an empty text
		String base2format = base2.replace("Continue reading...", "");

        String base3format = base2format.replace("Visit on site http://www.noobslab.com", "");

        String base4format = base3format.replace("Read More", "");

        String base5format = base4format.replace("(read more)", "");

        //load the article inside a text view
        TextView articletext = (TextView) rootView.findViewById(R.id.webv);

        //set the articles text
		articletext.setText(base5format);

        //set the article text size according to preferences
        articletext.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        return rootView;

	}

    //share method
    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share) + " '" + fFeed.getItem(fPos).getTitle() + "'"));
    }

    //Read more method
    private void continueReading() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getLink()));
        CharSequence title2 = getResources().getText(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title2);
        startActivity(chooser);
    }

    //back navigation method
    private void goBack() {

        //Cast getActivity() to AppCompatActivity to have access to support appcompat methods (onBackPressed();)
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.onBackPressed();
    }
}