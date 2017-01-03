package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

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

                        //make a rounded ImageView
                        .into(new BitmapImageViewTarget(where) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);

                                circularBitmapDrawable.setCornerRadius(10);

                                where.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        });

    }
}