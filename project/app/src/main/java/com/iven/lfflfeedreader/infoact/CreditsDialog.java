package com.iven.lfflfeedreader.infoact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iven.lfflfeedreader.R;

public class CreditsDialog extends DialogFragment {

    public static CreditsDialog create(int accentColor) {
        CreditsDialog dialog = new CreditsDialog();
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
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())

                .positiveColorRes(R.color.material_red_400)
                .titleColorRes(R.color.material_red_400)
                .dividerColorRes(R.color.lffl8)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .title(R.string.info)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .contentLineSpacing(1.6f)
                .build();

        final WebView webView = (WebView) customView.findViewById(R.id.dialog);

        WebSettings websettings = webView.getSettings();
        websettings.setDefaultTextEncodingName("utf-8");
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_res/raw/credits.html");
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         String link = url.replace("file:///'", "");
                                         String linkform = link.replace("/'", "");
                                         Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkform)
                                         );
                                         CharSequence title2 = getResources().getText(R.string.chooser_title);
                                         Intent chooser = Intent.createChooser(intent, title2);
                                         startActivity(chooser);
                                         view.loadUrl(url);
                                         return true;

                                     }
                                     @Override
                                     public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                                         try {
                                             webView.stopLoading();
                                         } catch (Exception e) {
                                         }
                                         try {
                                             webView.clearView();
                                         } catch (Exception e) {
                                         }
                                         if (webView.canGoBack()) {
                                             webView.goBack();
                                         }
                                         webView.loadUrl("file:///android_res/raw/credits.html");
                                         super.onReceivedError(webView, errorCode, description, failingUrl);
                                     }
                                 }
        );
        return dialog;
    }
}
