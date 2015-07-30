package com.iven.lfflfeedreader.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class AppUtils {

        public static int getThemedResId(Context context, int attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        final int resId = a.getResourceId(0, 0);
        a.recycle();
        return resId;
    }
}
