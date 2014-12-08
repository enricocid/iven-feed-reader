package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.imageparserutils.ImageLoader;
import com.iven.lfflfeedreader.infoact.InfoActivity;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class ListActivity extends Activity {

	RSSFeed feed;
	ListView list;
	CustomListAdapter adapter;
	String feedURL;
	private FadingActionBarHelper helper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		helper = new FadingActionBarHelper()
        .actionBarBackground(R.drawable.ab_background)
        .headerLayout(R.layout.fab_header)
        .contentLayout(R.layout.lffl_feed_list);
         setContentView(helper.createView(this));
         helper.initActionBar(this);
        

		feedURL = new SplashActivity().LFFLFEEDURL;

		feed = (RSSFeed) getIntent().getExtras().get("feed");

		list = (ListView) findViewById(android.R.id.list);
		adapter = new CustomListAdapter(this);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2;

				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				Intent intent = new Intent(ListActivity.this,
						ArticleActivity.class);
				intent.putExtras(bundle);
				intent.putExtra("pos", pos-1);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_main, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh_option:
			refreshList(item);
			return (true);


		case R.id.share_option:
        	Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, ("Lffl Feed Reader"));
            startActivity(Intent.createChooser(i, getString(R.string.share)));
	        return true;
		case R.id.about_option:
			startActivity(new Intent(this, InfoActivity.class));
			return (true);
		
	case R.id.rate:
		rate(list);
		return (true);
	}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void rate(View view) {
		  Intent intent = new Intent(Intent.ACTION_VIEW);
		  intent.setData(Uri.parse("market://details?id=com.iven.lfflfeedreader"));
		  startActivity(intent);
		}
	public void refreshList(final MenuItem item) {
		LayoutInflater inflater = (LayoutInflater) getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView lfflImage = (ImageView) inflater.inflate(R.layout.action_refresh,
				null);

		Animation rotation = AnimationUtils.loadAnimation(getApplication(),
				R.anim.refresh_rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		lfflImage.startAnimation(rotation);

		item.setActionView(lfflImage);

		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				DOMParser tmpDOMParser = new DOMParser();
				feed = tmpDOMParser.parseXml(feedURL);

				ListActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (feed != null && feed.getItemCount() > 0) {
							adapter.notifyDataSetChanged();
							item.getActionView().clearAnimation();
							item.setActionView(null);
						}
					}
				});
			}
		});
		thread.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.imageLoader.clearCache();
		adapter.notifyDataSetChanged();
	}

	class CustomListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		public ImageLoader imageLoader;

		public CustomListAdapter(ListActivity activity) {

			layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		@Override
		public int getCount() {

			return feed.getItemCount();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View listItem = convertView;
			int pos = position;
			if (listItem == null) {
				listItem = layoutInflater.inflate(R.layout.items, null);
			}

			ImageView lfflImage = (ImageView) listItem.findViewById(R.id.thumb);
			TextView lfflTitle = (TextView) listItem.findViewById(R.id.title);
			TextView pubDate = (TextView) listItem.findViewById(R.id.date);

			imageLoader.DisplayImage(feed.getItem(pos).getImage(), lfflImage);
			lfflTitle.setText(feed.getItem(pos).getTitle());
			pubDate.setText(feed.getItem(pos).getDate());

			return listItem;
		}

	}

}
