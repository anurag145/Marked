package com.github.anurag145.impulseattendance.geofence;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 573;

    private static final String TAG = "GeofenceTransitionsIS";

    private static final String CHANNEL_ID = "channel_01";
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
    public static void enqueueWork(Context context, Intent intent)
    {

    }


}
