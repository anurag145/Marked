package com.github.anurag145.impulseattendance.geofence;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.anurag145.impulseattendance.R;
import com.github.anurag145.impulseattendance.helper.Constants;
import com.github.anurag145.impulseattendance.helper.NotificationBuilder;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Map;

public class GeofenceForegroundServiceHandler extends Service implements OnCompleteListener<Void> {

    private static final String LOG_TAG = "GeofenceForeground";
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private   NotificationCompat.Builder myBuilder;
    @Override
    public void onCreate() {
        super.onCreate();
        Constants.ob= this;
        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
        populateGeofenceList();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
    }
    private void populateGeofenceList()
    {
        for (Map.Entry<String, LatLng> entry : Constants.OfficeLocation.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                     .setCircularRegion(
                           entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                          Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
       builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);

        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)throws SecurityException
    {

        try {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                if(checkPermissions()) {
                    myBuilder = NotificationBuilder.showNotificationForeground(this, intent, flags, startId);
                    startForeground(Constants.NOTIFICATION_ID,
                            myBuilder.build());
                    mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                            .addOnCompleteListener(this);


                }else
                { myBuilder = NotificationBuilder.showNotification(this,this.getResources().getString(R.string.insufficient_permissions));
                    NotificationManagerCompat.from(this).notify(Constants.NOTIFICATION_ID, myBuilder.build());
                }
            } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
                Log.i(LOG_TAG, "Clicked Previous");
            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
                Log.i(LOG_TAG, "Clicked Play");
            } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
                Log.i(LOG_TAG, "Clicked Next");
            } else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
                myBuilder = NotificationBuilder.showNotification(this,this.getResources().getString(R.string.geofencing_stoped));
                NotificationManagerCompat.from(this).notify(Constants.NOTIFICATION_ID, myBuilder.build());
                stopForeground(true);
                stopSelf();
            }


        }catch (Exception e)
        {
            Log.i("Tag",e.getMessage());
        }



        return START_STICKY;
    }
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("I am ", "dead");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
        updateGeofencesAdded(!getGeofencesAdded());

          if(!getGeofencesAdded())
          {
              stopForeground(false);
              NotificationBuilder.showNotification(this,this.getResources().getString(R.string.geofencing_stoped));
              stopSelf();
          }else
          {
              Toast.makeText(getApplicationContext(),"geofence started",Toast.LENGTH_SHORT).show();
          }


        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
                Log.d("OnCompleteError",errorMessage);
        }




    }
}
