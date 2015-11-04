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
import android.widget.Button;
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

        fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Preferences.applyTheme(getActivity());
        float size = Preferences.resolveTextSizeResId(getContext());

        ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.article_fragment, container, false);

		TextView title = (TextView) rootView.findViewById(R.id.title);
		TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);
		title.setText(fFeed.getItem(fPos).getTitle());
        subtitle.setText(fFeed.getItem(fPos).getAuthor() + " - " + fFeed.getItem(fPos).getDate());

		ImageView imageView = (ImageView) rootView.findViewById(R.id.img);

			Glide.with(getActivity()).load(fFeed.getItem(fPos).getImage())
					.diskCacheStrategy(DiskCacheStrategy.NONE)
					.into(imageView);

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

		String base2 = Jsoup.parse(fFeed.getItem(fPos).getDescription()).text().replace("Continua a leggere...", "");

		String base2format = base2.replace("Continue reading...", "");

        String base3format = base2format.replace("Visit on site http://www.noobslab.com", "");

		TextViewEx articletext = (TextViewEx) rootView.findViewById(R.id.webv);

		articletext.setText(base3format);

			Button continue_reading = (Button) rootView.findViewById(R.id.button);

            continue_reading.setTextSize(TypedValue.COMPLEX_UNIT_SP, size -5);

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

        articletext.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size + 4);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 5);

		Button share_button = (Button) rootView.findViewById(R.id.button_share);

        share_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, size -5);

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