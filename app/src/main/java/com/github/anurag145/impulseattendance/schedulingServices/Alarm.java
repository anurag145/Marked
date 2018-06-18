package com.github.anurag145.impulseattendance.schedulingServices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.github.anurag145.impulseattendance.geofence.GeofenceForegroundServiceHandler;
import com.github.anurag145.impulseattendance.helper.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm extends BroadcastReceiver {
    static Alarm alarm = new Alarm();

    public static Alarm getSingleton() {
        return alarm;
    }

    @Override

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I am here", Toast.LENGTH_SHORT).show();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire(10000);

        Intent startIntent = new Intent(context, GeofenceForegroundServiceHandler.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        context.startService(startIntent);
        wl.release();

    }

    public long timeCalulatorCheckIn(Context context) {
        try {
            String time = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.ALARM_CHECKIN, "15:07");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date timeForGeofence = new SimpleDateFormat("HH:mm").parse(time);
            Date currentTime = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
            Calendar calTimeForGeofence = Calendar.getInstance();
            calTimeForGeofence.setTime(timeForGeofence);
            Calendar calCurrentTime = Calendar.getInstance();
            calCurrentTime.setTime(currentTime);
            long m = calTimeForGeofence.getTimeInMillis() - calCurrentTime.getTimeInMillis();
            if (timeForGeofence.before(currentTime)) {
               m=24*60*60*1000+m;
                Log.i("timeCalculatorCheckIn", Long.toString(m));

            }


            return m;

        } catch (Exception e) {
            Log.i("timeCalculatorCheckIn", e.getMessage());
        }
        return 0;
    }

    public void setAlarmCheckIn(final Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckIn(context), pi);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckIn(context), pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckIn(context), pi);
        }
        Toast.makeText(context, "Alarm set", Toast.LENGTH_SHORT).show();

    }

    public void setAlarmCheckOut(final Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckOut(), pi);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckIn(context), pi);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeCalulatorCheckIn(context), pi);
        }
        Log.i( "Alarm set checkout", "ho gaya");

    }


    public long timeCalulatorCheckOut() {


           return 8*60*60*1000;

}

}