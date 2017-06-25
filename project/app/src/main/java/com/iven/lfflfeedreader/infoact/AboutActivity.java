package com.iven.lfflfeedreader.infoact;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.iven.lfflfeedreader.BuildConfig;
import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.utils.Preferences;

public class AboutActivity extends AppCompatActivity {

    //ContextThemeWrapper
    ContextThemeWrapper themeWrapper;

    //get all the TextViews
    TextView dev, enricoGit, enricoGplus, credits, ivanGit, ivanGplus, stack, lucas, support4, appCompat, recyclerView, cardView, jsoup, glide, libraries, appInfo, build, sources;

    //Get all the Strings
    String enricoGitPage, enricoGplusPage, ivanGitPage, ivanGplusPage, stackPage, lucasPage, support4page, appCompatPage, recyclerViewPage, cardViewPage, jsoupPage, glidePage, version, appGit;

    //method to set clickable links
    @SuppressWarnings("deprecation")
    static Spanned setTextLinks(String htmlText, TextView... textViews) {

        for (TextView links : textViews) {
            links.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);

        } else {
            return Html.fromHtml(htmlText);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
        themeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());

        Preferences.applyTheme(themeWrapper, getBaseContext());

        //apply light status bar icons if enabled
        Preferences.applyLightIcons(this);

        Preferences.applyNavTint(this);

        setContentView(R.layout.activity_about);

        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String colon = ": ";

        //get Titles
        dev = (TextView) findViewById(R.id.dev);

        credits = (TextView) findViewById(R.id.credits);

        libraries = (TextView) findViewById(R.id.libs);

        appInfo = (TextView) findViewById(R.id.app_info);

        //set text options to titles
        setTextUnderline(dev, credits, libraries, appInfo);

        //Developer info
        //git page
        enricoGit = (TextView) findViewById(R.id.enrico_git);

        enricoGitPage = "<a href='https://github.com/enricocid'>github";

        enricoGit.setText(setTextLinks(enricoGitPage, enricoGit));

        //gplus page
        enricoGplus = (TextView) findViewById(R.id.enrico_gplus);

        enricoGplusPage = "<a href='https://plus.google.com/u/0/+EnricoDortenzio/'>google+";

        enricoGplus.setText(setTextLinks(enricoGplusPage, enricoGplus));

        //Credits
        //Ivan

        //git page
        ivanGit = (TextView) findViewById(R.id.ivan_git);

        ivanGitPage = "<a href='https://github.com/ivn888/'>github";

        ivanGit.setText(setTextLinks(ivanGitPage, ivanGit));

        //gplus page
        ivanGplus = (TextView) findViewById(R.id.ivan_gplus);

        ivanGplusPage = "<a href='https://plus.google.com/u/0/+ivandortenzio/'>google+";

        ivanGplus.setText(setTextLinks(ivanGplusPage, ivanGplus));

        //stackoverflow
        stack = (TextView) findViewById(R.id.stack_page);

        stackPage = "<a href='http://stackoverflow.com/'>home";

        stack.setText(setTextLinks(stackPage, stack));

        //Lucas Urbas
        lucas = (TextView) findViewById(R.id.lucas_page);

        lucasPage = "<a href='https://medium.com/@lucasurbas/making-android-toolbar-responsive-2627d4e07129/'>responsive toolbar";

        lucas.setText(setTextLinks(lucasPage, lucas));

        //Libraries used

        //v4 support
        support4 = (TextView) findViewById(R.id.v4_page);

        support4page = "<a href='https://developer.android.com/topic/libraries/support-library/index.html'>page";

        support4.setText(setTextLinks(support4page, support4));

        //appcompat
        appCompat = (TextView) findViewById(R.id.appcompat_page);

        appCompatPage = "<a href='https://developer.android.com/topic/libraries/support-library/features.html'>page";

        appCompat.setText(setTextLinks(appCompatPage, appCompat));

        //recyclerView
        recyclerView = (TextView) findViewById(R.id.recyclerView_page);

        recyclerViewPage = "<a href='https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html'>page";

        recyclerView.setText(setTextLinks(recyclerViewPage, recyclerView));

        //cardView
        cardView = (TextView) findViewById(R.id.card_page);

        cardViewPage = "<a href='https://developer.android.com/reference/android/support/v7/widget/CardView.html'>page";

        cardView.setText(setTextLinks(cardViewPage, cardView));

        //jsoup
        jsoup = (TextView) findViewById(R.id.soup_page);

        jsoupPage = "<a href='https://github.com/jhy/jsoup'>github";

        jsoup.setText(setTextLinks(jsoupPage, jsoup));

        //glide
        glide = (TextView) findViewById(R.id.glide_page);

        glidePage = "<a href='https://github.com/bumptech/glide'>github";

        glide.setText(setTextLinks(glidePage, glide));

        //Application info
        //set build version
        version = BuildConfig.VERSION_NAME;

        build = (TextView) findViewById(R.id.version);

        build.setText(getString(R.string.version) + colon + version);

        //set application git page
        sources = (TextView) findViewById(R.id.sources);

        appGit = "<a href='https://github.com/enricocid/iven-feed-reader'>github";

        sources.setText(setTextLinks(appGit, sources));

        //set text options
        setTextOptions(enricoGit, enricoGplus, ivanGit, build, sources);
    }

    //set text options
    public void setTextOptions(TextView... textView) {

        for (TextView body : textView) {
            body.setElegantTextHeight(true);
        }
    }

    //method to style headlines
    public void setTextUnderline(TextView... textView) {

        for (TextView title : textView) {
            title.setElegantTextHeight(true);
            title.setElevation(6);
            title.setFontFeatureSettings("smcp"); //small caps
            title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    //close app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
