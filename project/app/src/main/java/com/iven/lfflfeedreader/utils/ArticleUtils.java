package com.iven.lfflfeedreader.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.mainact.ArticlePage;

public class ArticleUtils {

    //share method
    public static void share(Activity activity, String feedLink, String feedTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, feedLink);
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share) + " '" + feedTitle + "'"));
    }

    //read more method
    public static void continueReading(Activity activity, String feedLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedLink));
        CharSequence title2 = activity.getResources().getText(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title2);
        activity.startActivity(chooser);
    }

    //back navigation method
    public static void goBack(AppCompatActivity appCompatActivity) {

        //Cast getActivity() to AppCompatActivity to have access to support appcompat methods (onBackPressed();)
        appCompatActivity.onBackPressed();
    }

    //method to open the feed link using a webview
    public static void openFeedPage(Activity activity, String datfeed) {
        //we send the url and the title using intents to ArticlePage activity
        final Intent intent = new Intent(activity, ArticlePage.class);

        intent.putExtra("feedselected", datfeed);

        //and start a new ArticlePage activity with the selected feed
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    //method to open the image link
    public static void openImageLink(Activity activity, Intent intent) {
        CharSequence title2 = activity.getResources().getText(R.string.chooser_title);
        Intent chooser = Intent.createChooser(intent, title2);
        activity.startActivity(chooser);
    }
}