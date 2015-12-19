package com.iven.lfflfeedreader.mainact;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.utils.Preferences;
import com.iven.lfflfeedreader.utils.ScrollAwareFABBehavior;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        //initialize the fab button
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.back);

        //initialize continue reading and share TextViews used for immersive mode
        final TextView txt_continue_reading = (TextView) rootView.findViewById(R.id.txt_continue);

        final TextView txt_share = (TextView) rootView.findViewById(R.id.txt_share);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        //remove fab button from the view if api < 19, i.e KitKat
        if (Build.VERSION.SDK_INT < 19){
            fab.setVisibility(View.INVISIBLE);
        }

        //only for api >=19, i.e KitKat
        //if immersive mode is enabled show a fab button dynamically to provide back navigation
        //since toolbar is now hidden in article activity

        if (Build.VERSION.SDK_INT >= 19){
        if (Preferences.immersiveEnabled(getActivity())) {

                //this the method to handle fab click to provide back navigation
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                };

                //set fab's on click listener
                fab.setOnClickListener(listener);

            //set fab's behavior
            //initialize coordinator layout
            final CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(new ScrollAwareFABBehavior());
            fab.setLayoutParams(p);

            }else {

            //set fab button not visible if immersive mode is disabled
            fab.setVisibility(View.INVISIBLE);

            }
        }


        //set continue reading and share TextViews text dynamically

        //continue reading
        SpannableString str_read = new SpannableString(getResources().getString(R.string.continue_read));

        str_read.setSpan(new UnderlineSpan(), 0, str_read.length(),
                Spanned.SPAN_PARAGRAPH);

        txt_continue_reading.setText(str_read);

        //share
        SpannableString str_share = new SpannableString(getResources().getString(R.string.share));

        str_share.setSpan(new UnderlineSpan(), 0, str_share.length(),
                Spanned.SPAN_PARAGRAPH);

        txt_share.setText(str_share);

        //this the method to handle the continue reading TextView click on immersive mode
        View.OnClickListener listener_forward = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReading();
                txt_continue_reading.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            }
        };

        //this the method to handle the share TextView click on immersive mode
        View.OnClickListener listener_share = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                txt_share.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            }
        };

        //set continue reading/share TextViews listeners
        txt_continue_reading.setOnClickListener(listener_forward);

        txt_share.setOnClickListener(listener_share);

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

        //if getImage() method fails (i.e when img is in content:encoded) load image2
        if (fFeed.getItem(fPos).getImage().isEmpty()) {

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

                    if (fFeed.getItem(fPos).getImage().isEmpty()) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fFeed.getItem(fPos).getImage2()));

                    }
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
        txt_share.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        txt_continue_reading.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

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