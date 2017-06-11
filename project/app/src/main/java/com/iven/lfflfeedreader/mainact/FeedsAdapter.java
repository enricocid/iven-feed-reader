package com.iven.lfflfeedreader.mainact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.domparser.RSSItem;
import com.iven.lfflfeedreader.utils.GlideUtils;
import com.iven.lfflfeedreader.utils.Preferences;

class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.SimpleViewHolder> {

    private Activity activity;

    private RSSFeed fFeed;

    FeedsAdapter(Activity activity, RSSFeed fFeed) {

        this.activity = activity;

        this.fFeed = fFeed;


    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items, parent, false);
        return new SimpleViewHolder(activity, itemView, fFeed);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        //get the chosen items text size from preferences

        RSSItem feedItem = fFeed.getItem(position);

        String imageLink = feedItem.getImage();
        String imageLink2 = feedItem.getImage2();
        String feedTitle = feedItem.getTitle();
        String feedDate = feedItem.getDate();
        int size = Preferences.resolveTextSizeListResId(activity);

        //dynamically set title and subtitle according to the feed data

        //title
        holder.itemTitle.setText(feedTitle);

        //subtitle = publication date
        holder.pubDate.setText(feedDate);
        holder.pubDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);

        //set the list items text size from preferences in SP unit
        holder.itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);


        //if the preference is enabled remove the linear layout containing the ImageView
        if (Preferences.imagesRemoved(activity)) {

            holder.linearLayout.removeAllViewsInLayout();

        }

        //else, load the image
        //if getImage() method fails (i.e when img is in content:encoded) load image2
        else if (imageLink.isEmpty()) {

            GlideUtils.loadImage(activity, imageLink2, holder.lfflImage);

            //else use image
        } else {

            GlideUtils.loadImage(activity, imageLink, holder.lfflImage);
        }

    }

    @Override
    public int getItemCount() {

        return fFeed.getItemCount();
    }

    //simple view holder implementing on click method to open feed page
    static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView itemTitle;
        TextView pubDate;
        ImageView lfflImage;
        LinearLayout linearLayout;
        private Activity activity;
        private RSSFeed fFeed;


        SimpleViewHolder(Activity activity, View itemView, RSSFeed fFeed) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.fFeed = fFeed;

            this.activity = activity;

            itemTitle = (TextView) itemView.findViewById(R.id.title);
            pubDate = (TextView) itemView.findViewById(R.id.date);
            lfflImage = (ImageView) itemView.findViewById(R.id.thumb);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout);

        }

        @Override
        public void onClick(View v) {

            //open feed page
            Bundle bundle = new Bundle();

            //set feed info to article activity
            bundle.putSerializable("feed", fFeed);
            Intent intent = new Intent(activity,
                    ArticleActivity.class);
            intent.putExtras(bundle);
            intent.putExtra("pos", getAdapterPosition());

            //start the article activity to read the story
            activity.startActivity(intent);

        }
    }
}