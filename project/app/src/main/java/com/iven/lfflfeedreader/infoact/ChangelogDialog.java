package com.iven.lfflfeedreader.infoact;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
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
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.changelog_layout, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())

                        .theme(Theme.DARK)
                        .positiveColorRes(R.color.material_red_400)
                        .titleGravity(GravityEnum.CENTER)
                        .titleColorRes(R.color.material_red_400)
                        .dividerColorRes(R.color.lffl8)
                        .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                        .positiveColor(Color.WHITE)
                        .title(R.string.changelog)
                        .customView(customView, false)
                        .positiveText(android.R.string.ok)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .build();

        final WebView webView = (WebView) customView.findViewById(R.id.changelog);
        WebSettings websettings = webView.getSettings();
        websettings.setDefaultTextEncodingName("utf-8");
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_res/raw/changelog.html");
        return dialog;
	}
}