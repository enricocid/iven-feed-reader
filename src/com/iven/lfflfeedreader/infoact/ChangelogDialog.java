package com.iven.lfflfeedreader.infoact;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.iven.lfflfeedreader.R;

/**
 * @author Enrico D'Ortenzio (EnricoD)
 */
/**
 * @Original_implementation_by Aidan Follestad (afollestad)
 */

public class ChangelogDialog extends DialogFragment {
	
	public static ChangelogDialog create(int accentColor) {
        ChangelogDialog dialog = new ChangelogDialog();
        Bundle args = new Bundle();
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.changelog_layout, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
        		.theme(Theme.LIGHT)
                .title(R.string.changelog)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .build();

        final WebView webView = (WebView) customView.findViewById(R.id.changelog);
            // Load from changelog.html in the raw folder
        webView.setBackgroundColor(Color.TRANSPARENT);
        	webView.loadUrl("file:///android_res/raw/changelog.html");

        return dialog;
	}
}