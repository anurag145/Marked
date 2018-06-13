package com.github.anurag145.impulseattendance.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.github.anurag145.impulseattendance.MainActivity;
import com.github.anurag145.impulseattendance.R;
import com.github.anurag145.impulseattendance.R;
import com.github.anurag145.impulseattendance.Service.NotificationService;

import static com.github.anurag145.impulseattendance.helper.Constants.CHANNEL_ID;

public class NotificationBuilder {
    private static final String LOG_TAG = "ForegroundService";
    public static NotificationCompat.Builder showNotification(Context context, Intent intent, int flags, int startId)
    {

            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            Intent previousIntent = new Intent(context, NotificationService.class);
            previousIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(context, 0,
                    previousIntent, 0);



            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                CharSequence name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
                String description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel =
                        new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                // Add the channel
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentTitle("Truiton Music Player")
                    .setTicker("Truiton Music Player")
                    .setContentText("My Music")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)

                    .setContentIntent(pendingIntent)
                    .setOngoing(true)

                    .addAction(android.R.drawable.ic_media_previous,
                            "Stop", ppreviousIntent);


            // Create the notification

           return notification;
            // Show the notification


        }

}
