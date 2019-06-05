package com.quizandroidtest.services;

/**
 * Created by user on 9/2/2016.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.quizandroidtest.MainActivity;
import com.quizandroidtest.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AlarmService extends BroadcastReceiver {
    private NotificationManager alarmNotificationManager;
    private final int UNIQUE_ID = 120192;
    private int mid = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public AlarmService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        pref = context.getSharedPreferences("Quiz", 0); // 0 - for private mode
        editor = pref.edit();
        sendNotification(intent.getStringExtra("msg"), context);

    }

    private void sendNotification(final String msg, final Context ctx) {
        editor.putString("time", "");
        editor.commit();
        alarmNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mid++;
        Intent notificationIntent = new Intent(ctx, MainActivity.class);
        notificationIntent.putExtra("name", msg);
        final PendingIntent contentIntent = PendingIntent.getActivity(ctx, UNIQUE_ID,
                notificationIntent, 0);




        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        final NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                ctx).setContentTitle("You are on the break").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText("").setSound(uri).setPriority(Notification.PRIORITY_HIGH).setContentIntent(contentIntent);


        final String NOTIFICATION_CHANNEL_ID = "10001";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            assert alarmNotificationManager != null;
            alamNotificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);

            alarmNotificationManager.createNotificationChannel(notificationChannel);
        }
        alamNotificationBuilder.setOngoing(true);
        final Notification notification = alamNotificationBuilder.build();

        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(mid, notification);


        long intervalTime = pref.getLong("intervalTime", 0);



        // Here Create Notification Timer

        CountDownTimer countDownTimer = new CountDownTimer(intervalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                editor.putLong("intervalTime", millisUntilFinished); // Storing string
                editor.commit();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));


                String msgContent = "Time Left:" + String.format(Locale.getDefault(), "%02d:%02d",

                        minutes,
                        seconds);

                final NotificationCompat.Builder aLamNotificationBuilder = new NotificationCompat.Builder(
                        ctx).setContentTitle("You are on the break").setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentText(msgContent).setPriority(Notification.PRIORITY_HIGH).setContentIntent(contentIntent);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_LOW;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(false);
                    notificationChannel.enableVibration(false);
                    assert aLamNotificationBuilder != null;
                    aLamNotificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    alarmNotificationManager.createNotificationChannel(notificationChannel);
                }
                aLamNotificationBuilder.setOngoing(true);
                alarmNotificationManager.notify(mid, aLamNotificationBuilder.build());
            }

            public void onFinish() {


                final NotificationCompat.Builder aLamNotificationBuilder = new NotificationCompat.Builder(
                        ctx).setContentTitle("You are on the break").setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentText("Done!").setPriority(Notification.PRIORITY_HIGH).setContentIntent(contentIntent);
                Log.d("AlarmService", "onTick sent.");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_LOW;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(false);
                    notificationChannel.enableVibration(false);
                    assert aLamNotificationBuilder != null;
                    aLamNotificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    alarmNotificationManager.createNotificationChannel(notificationChannel);
                }
                aLamNotificationBuilder.setOngoing(true);
                alarmNotificationManager.notify(mid, aLamNotificationBuilder.build());
            }
        };
        countDownTimer.start();
    }


}