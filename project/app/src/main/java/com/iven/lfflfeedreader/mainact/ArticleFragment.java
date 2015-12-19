package com.iven.lfflfeedreader.mainact;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
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

        //get the chosen article's text size from preferences
        float size = Preferences.resolveTextSizeResId(getContext());

                //set the view
                ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.article_fragment, container, false);

        //initialize the article view linear layout
        LinearLayout article_default_linearLayout = (LinearLayout) rootView.findViewById(R.id.article_linearlayout);

        //initialize items (title, subtitle, read more, share, buttons, layouts ...)

        //read more button
        ImageButton button_continue_reading = (ImageButton) rootView.findViewById(R.id.button_continue);

        //share button
        ImageButton button_share = (ImageButton) rootView.findViewById(R.id.button_share);

        //back button
        ImageButton button_back = (ImageButton) rootView.findViewById(R.id.button_back);

        //title
        TextView title = (TextView) rootView.findViewById(R.id.title);

        //subtitle
        TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);

        //text view under read more button
        TextView continue_default = (TextView) rootView.findViewById(R.id.txt_continue);

        //text view under share button
        TextView share_default = (TextView) rootView.findViewById(R.id.txt_share);

        //text view under read more button in immersive mode
        TextView continue_immersed = (TextView) rootView.findViewById(R.id.txt_continue_immersed);

        //text view under share button in immersive mode
        TextView share_immersed = (TextView) rootView.findViewById(R.id.txt_share_immersed);

        //text view under back button in immersive mode
        TextView back_text_immersed = (TextView) rootView.findViewById(R.id.txt_back_immersed);

        //initialize article's imageview
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

        //percentRelativeLayout containing action buttons
        PercentRelativeLayout article_percentlayout = (PercentRelativeLayout) rootView.findViewById(R.id.action_buttons);
        PercentRelativeLayout article_percentlayout_immersed = (PercentRelativeLayout) rootView.findViewById(R.id.action_buttons_immersed);

        //remove the back button from the view if api < 21, i.e Lollipop
        //since immersive mode is not available on pre-ics
        //and toolbar hide method is not working on KitKat
        if (Build.VERSION.SDK_INT < 21){
            article_default_linearLayout.removeView(article_percentlayout_immersed);
        }

        //Cast getActivity() to AppCompatActivity to have access to support appcompat methods (onBackPressed();)
        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        //only for api >=21, i.e Lollipop since toolbar hide method is not working on KitKat
        //if immersive mode is enabled show a back button dynamically to provide back navigation
        //since toolbar is now hidden in article activity

        if (Build.VERSION.SDK_INT >= 21){
        if (Preferences.immersiveEnabled(getActivity())) {

            //set default action buttons not visible if immersive mode is disabled
            //live only the immersed action buttons with back button to provide back navigation
            article_default_linearLayout.removeView(article_percentlayout);

            }else {

            //set immersed actions buttons not visible if immersive mode is disabled
            //live only the default action buttons (Read more... and Share buttons)
            article_default_linearLayout.removeView(article_percentlayout_immersed);

            }
        }

        //this the method to handle the back button click to provide back navigation
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        };

        //set back button on click listener
        button_back.setOnClickListener(listener);

        //this is the method to handle the continue reading button click
        View.OnClickListener listener_forward = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReading();
            }
        };

        //this is the method to handle the share button click
        View.OnClickListener listener_share = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        };

        //set continue reading/share buttons listeners
        button_continue_reading.setOnClickListener(listener_forward);

        button_share.setOnClickListener(listener_share);

        //dynamically set title and subtitle according to the feed data

        //title
        title.setText(fFeed.getItem(fPos).getTitle());

        //add date to subtitle
        subtitle.setText(fFeed.getItem(fPos).getDate());

        //if the preference is enabled remove the imageview from the linear layout
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
                    else
                    {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getImage()));

                    }
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

        //load the article inside a text view
        TextView articletext = (TextView) rootView.findViewById(R.id.webv);

        //set the articles text
		articletext.setText(base3format);

        //set the articles text size from preferences
        //little explanation about setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        // TypedValue.COMPLEX_UNIT_SP = the text unit, in this case SP
        // size = the text size from preferences

        articletext.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);
        continue_default.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        share_default.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        continue_immersed.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        share_immersed.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        back_text_immersed.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        return rootView;

	}

    public void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fFeed.getItem(fPos).getLink());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share) + " '" + fFeed.getItem(fPos).getTitle() + "'"));
    }

    public void continueReading() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getLink()));
        CharSequence title2 = getResources().getText(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title2);
        startActivity(chooser);
    }
}