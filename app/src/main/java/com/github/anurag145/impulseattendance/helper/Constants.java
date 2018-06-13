package com.github.anurag145.impulseattendance.helper;

import java.util.HashMap;


import com.github.anurag145.impulseattendance.geofence.GeofenceForegroundServiceHandler;
import com.google.android.gms.maps.model.LatLng;
public class Constants {
    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";
    public static final CharSequence NOTIFICATION_TITLE = "WorkRequest Starting";
    public static final String CHANNEL_ID = "VERBOSE_NOTIFICATION" ;
    public static final int NOTIFICATION_ID = 1;

    public interface ACTION {
        public static String MAIN_ACTION = "com.github.anurag145.impulseattendance.action.main";
        public static String PREV_ACTION = "com.github.anurag145.impulseattendance.action.prev";
        public static String PLAY_ACTION = "com.github.anurag145.impulseattendance.action.play";
        public static String NEXT_ACTION = "com.github.anurag145.impulseattendance.action.next";
        public static String STARTFOREGROUND_ACTION = "com.github.anurag145.impulseattendance.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.github.anurag145.impulseattendance.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
    public static final String PACKAGE_NAME = "com.github.anurag145.impulseattendance";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 2;
     public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
  public   static final float GEOFENCE_RADIUS_IN_METERS = 50; // 1 mile, 1.6 km

    /**
     * Map for storing information about Offices in Gurgoan
     */
     public  static  final HashMap<String, LatLng> OfficeLocation = new HashMap<>();

    static {

        OfficeLocation.put("SFO", new LatLng(28.430967, 77.01433999999));


        OfficeLocation.put("GOOGLE", new LatLng(37.422611,-122.0840577));


    }
    public static GeofenceForegroundServiceHandler ob =null;
}
