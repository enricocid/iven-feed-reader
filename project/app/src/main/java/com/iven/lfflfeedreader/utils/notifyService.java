package com.iven.lfflfeedreader.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.iven.lfflfeedreader.R;
import com.iven.lfflfeedreader.domparser.DOMParser;
import com.iven.lfflfeedreader.domparser.RSSFeed;
import com.iven.lfflfeedreader.domparser.RSSItem;
import com.iven.lfflfeedreader.mainact.SplashActivity;


public class notifyService extends Service {

    public static final String PARAM_IN_MSG = "imsg";

    //all items
    int lastDate;
    String firstItemDate;
    Handler handler;
    Runnable runnableCode;
    DOMParser tmpDOMParser;
    RSSFeed fFeed;
    RSSItem updatedFeedItem;
    String updatedDate;
    String updatedDateFormat;
    int updatedLastDate;
    Intent broadcastIntent;
    PendingIntent pi;
    Uri notificationSound;
    Notification serviceNotification;
    Notification notification;
    NotificationManager notificationManager;
    SharedPreferences getAlarms;
    String alarm;
    Uri defaultRingtoneUri;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //make a foreground notification to avoid android kills the service
        serviceNotification = new NotificationCompat.Builder(this)
                .setOngoing(false)

                .build();
        startForeground(101, serviceNotification);

        //get selected notification sound
        getAlarms = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_NOTIFICATION);

        alarm = getAlarms.getString("audio", String.valueOf(defaultRingtoneUri));

        //get last item date from ListActivity
        firstItemDate = intent.getStringExtra(PARAM_IN_MSG);
        saveUtils.saveLastDate(getBaseContext(), firstItemDate);

        lastDate = Integer.valueOf(firstItemDate);

        //get selected notification
        notificationSound = Uri.parse(alarm);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if (Preferences.notificationsEnabled(getBaseContext())) {

            //the service will be restarted if killed
            broadcastIntent = new Intent("dontKillMe");
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Thread thread = new Thread(new Runnable() {

            public void run() {

                Looper.prepare();

                handler = new Handler();

                // Define the code block to be executed
                runnableCode = new Runnable() {
                    @Override
                    public void run() {

                        //parse xml to check if there are new articles
                        tmpDOMParser = new DOMParser();
                        fFeed = tmpDOMParser.parseXml(saveUtils.getFeedUrl(getBaseContext()));

                        //get the date of the last article posted
                        updatedFeedItem = fFeed.getItem(0);
                        updatedDate = updatedFeedItem.getDate();

                        updatedDateFormat = updatedDate.substring(updatedDate.length() - 5).replace(":", "");
                        updatedLastDate = Integer.valueOf(updatedDateFormat);

                        Log.d("firstDate", String.valueOf(lastDate));
                        Log.d("updatedDate", String.valueOf(updatedLastDate));

                        if (updatedLastDate != lastDate) {

                            pi = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), SplashActivity.class), 0);

                            //build notification
                            notification = new NotificationCompat.Builder(getBaseContext())
                                    .setSmallIcon(android.R.drawable.stat_notify_more)
                                    .setContentText(getString(R.string.news))
                                    .setContentIntent(pi)
                                    .setAutoCancel(true)
                                    .setColor(ContextCompat.getColor(getBaseContext(), R.color.accent_color))
                                    .setSound(notificationSound)
                                    .build();

                            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);

                            saveUtils.saveLastDate(getBaseContext(), updatedDate);
                        }

                        // Repeat this the same runnable code block again another tot seconds (defined by the user)
                        handler.postDelayed(runnableCode, Preferences.resolveTime(getBaseContext()) * 1000);
                    }
                };

                // Start the initial runnable task by posting through the handler
                handler.post(runnableCode);

                Looper.loop();
            }
        });

        thread.start();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);

    }
}
