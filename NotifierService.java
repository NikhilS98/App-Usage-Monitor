package com.android.ang.seprocessmonitor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.ang.seprocessmonitor.App.CHANNEL_ID;

public class NotifierService extends Service {

    private NotificationManagerCompat notificationManagerCompat;
    private Notification usageNotification;
    private PendingIntent usagePendingIntent;
    private Intent usageNotificationIntent;
    private DeviceApps deviceApps;
    private DoubleObject[] appsRecommendedUsage;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManagerCompat = NotificationManagerCompat.from(this);

        deviceApps = new DeviceApps(this);

        appsRecommendedUsage = new DoubleObject[7];
        //appsRecommendedUsage[0] = new DoubleObject("whatsapp", (long) 29);
        appsRecommendedUsage[0] = new DoubleObject("chrome", (long) 60);
        appsRecommendedUsage[1] = new DoubleObject("skype", (long) 16);
        appsRecommendedUsage[2] = new DoubleObject("messenger", (long) 10);
        appsRecommendedUsage[3] = new DoubleObject("facebook", (long) 41);
        appsRecommendedUsage[4] = new DoubleObject("instagram", (long) 53);
        appsRecommendedUsage[5] = new DoubleObject("snapchat", (long) 50);
        appsRecommendedUsage[6] = new DoubleObject("youtube", (long) 60);
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, to wake up every 30 secs
        timer.schedule(timerTask, 2000, 30000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        usageNotificationIntent = new Intent(this, ExceededUsageApps.class);
        usagePendingIntent = PendingIntent.getActivity(this, 0, usageNotificationIntent, 0);

        Notification runningNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Process Monitor")
                .setContentText("Running")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, runningNotification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, NotifierRestarter.class);

        stoptimertask();
        sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                deviceApps.dailyUsage();
                ArrayList<String> appNameList = deviceApps.getAppNameList();
                ArrayList<Long> appUsageMilliList = deviceApps.getUsageTimeMilliList();

                for(int i = 0; i < appNameList.size(); i++) {
                    appNameList.set(i, appNameList.get(i).toLowerCase());
                }

                for(int i = 0; i < appsRecommendedUsage.length; i++) {
                    String appName = (String) appsRecommendedUsage[i].getObject1();
                    int index = appNameList.indexOf(appName);

                    if(index > -1 && !appsRecommendedUsage[i].isNotified()) {
                        long usageMinutes = (long) appsRecommendedUsage[i].getObject2();
                        long usedMS = appUsageMilliList.get(index);

                        long ms = usageMinutes * 60 * 1000;
                        if(usedMS >= ms) {
                            appsRecommendedUsage[i].setNotified(true);
                            createUsageNotification(appName, usageMinutes, i + 2);
                        }
                    }
                }
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void createUsageNotification(String appName, long minutes, int id) {
        usageNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Process Monitor")
                .setContentText("You have exceed usage limit of " + minutes + " minutes for " + appName + ". Click here for more info")
                .setSmallIcon(R.drawable.ic_announcement)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(usagePendingIntent)
                .build();

        notificationManagerCompat.notify(id, usageNotification);

    }
}
