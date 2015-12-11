package com.iven.lfflfeedreader.infoact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
                .title(R.string.info)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .backgroundColorRes(R.color.dialog_bg)
                .contentLineSpacing(1.6f)
                .build();

        //initialize the webview
        final WebView webView = (WebView) customView.findViewById(R.id.dialog);

        //initialize the webview settings
        WebSettings websettings = webView.getSettings();

        //set default encoding to utf-8 to avoid malformed text
        websettings.setDefaultTextEncodingName("utf-8");

        //set bg transparent because we will apply the bg using the activity's theme
        webView.setBackgroundColor(Color.TRANSPARENT);

        // Load from credits.html in the raw folder
        webView.loadUrl("file:///android_res/raw/credits.html");

        //override this method to load the urls inside external browser
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                         //replace some text to avoid malformed urls
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

                                         //load from credits.html when we go back from browser
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
