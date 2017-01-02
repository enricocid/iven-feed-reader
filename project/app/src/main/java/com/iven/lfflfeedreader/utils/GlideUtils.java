package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideUtils {

    public static void loadImage(final Activity activity, final String who, final ImageView where) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(activity).load(who)

                        //load images as bitmaps to get fixed dimensions
                        .asBitmap()

                        //disable cache to avoid garbage collection that may produce crashes
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                        .into(where);
            }
        });

    }
}