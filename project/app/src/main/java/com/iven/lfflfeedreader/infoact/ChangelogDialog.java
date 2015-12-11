package com.iven.lfflfeedreader.infoact;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iven.lfflfeedreader.R;
import android.annotation.SuppressLint;

public class ChangelogDialog extends DialogFragment {

	public static ChangelogDialog create(int accentColor) {
        ChangelogDialog dialog = new ChangelogDialog();
        Bundle args = new Bundle();
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            //set the view
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }

        //create new material dialog
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())

                //see material dialog docs for more info
                //https://github.com/afollestad/material-dialogs
                        .titleColorRes(R.color.dialog_title)
                        .positiveColor(ContextCompat.getColor(getContext(), R.color.primary))
                        .title(R.string.changelog)
                        .customView(customView, false)
                        .positiveText(android.R.string.ok)
                        .backgroundColorRes(R.color.dialog_bg)
                .build();

        //initialize the webview
        final WebView webView = (WebView) customView.findViewById(R.id.dialog);

        //initialize the webview settings
        WebSettings websettings = webView.getSettings();

        //set default encoding to utf-8 to avoid malformed text
        websettings.setDefaultTextEncodingName("utf-8");

        //set bg transparent because we will apply the bg using the activity's theme
        webView.setBackgroundColor(Color.TRANSPARENT);

        // Load from changelog.html in the raw folder
        webView.loadUrl("file:///android_res/raw/changelog.html");
        return dialog;
	}
}