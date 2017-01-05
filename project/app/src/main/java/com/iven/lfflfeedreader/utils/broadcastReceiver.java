package com.iven.lfflfeedreader.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class broadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent restartService = new Intent(context, notifyService.class);

        //send last date to restart notify service
        restartService.putExtra(notifyService.PARAM_IN_MSG, saveUtils.getLastDate(context));

        context.startService(restartService);
    }
}
