package com.github.anurag145.impulseattendance.geofence;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import com.github.anurag145.impulseattendance.R;
import com.github.anurag145.impulseattendance.helper.Constants;
import com.github.anurag145.impulseattendance.helper.NotificationBuilder;
import com.github.anurag145.impulseattendance.schedulingServices.Alarm;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 573;

    private static final String TAG = "GeofenceTransitionsIS";

    private static final String CHANNEL_ID = "channel_01";
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {


            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();


            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences);

            Constants.ob.stopForeground(true);
            Constants.ob.terminatedByUser=false;
            Constants.ob.mGeofencingClient.removeGeofences(Constants.ob.getGeofencePendingIntent()).addOnCompleteListener(Constants.ob);
            NotificationCompat.Builder myBuilder = NotificationBuilder.showNotification(getApplicationContext(), geofenceTransitionDetails);
            NotificationManagerCompat.from(this).notify(Constants.NOTIFICATION_ID, myBuilder.build());
            Alarm.getSingleton().setAlarmCheckOut(this);
            Constants.ob.stopSelf();

            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("I am ", "dead too");
    }

    public static void enqueueWork(Context context, Intent intent)
    {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }



    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}
