package com.iven.lfflfeedreader.infoact;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.iven.lfflfeedreader.R;

public class AboutDialog extends DialogFragment {

    public static void show(AppCompatActivity context) {
        AboutDialog dialog = new AboutDialog();
        dialog.show(context.getSupportFragmentManager(), "[ABOUT_DIALOG]");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .theme(Theme.DARK)
                .positiveColor(Color.WHITE)
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(R.color.material_red_400)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .dividerColorRes(R.color.lffl8)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .positiveText(android.R.string.ok)
                .title(R.string.about)
                .content(Html.fromHtml(getString(R.string.about_body)))
                .contentLineSpacing(1.6f)
                .build();
    }
}
