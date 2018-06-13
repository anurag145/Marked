package com.github.anurag145.impulseattendance.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.github.anurag145.impulseattendance.R;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.github.anurag145.impulseattendance.MainActivity;
import com.github.anurag145.impulseattendance.helper.Constants;
import com.github.anurag145.impulseattendance.helper.NotificationBuilder;

public class NotificationService extends Service {

    private static final String LOG_TAG = "ForegroundService";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                NotificationCompat.Builder myBuilder = NotificationBuilder.showNotification(getApplicationContext(), intent, flags, startId);

                startForeground(Constants.NOTIFICATION_ID,
                        myBuilder.build());
            } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                Log.i(LOG_TAG, "Clicked Previous");
            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                Log.i(LOG_TAG, "Clicked Play");
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                Log.i(LOG_TAG, "Clicked Next");
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }


        }catch (Exception e)
        {
            Log.i("Tag",e.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}