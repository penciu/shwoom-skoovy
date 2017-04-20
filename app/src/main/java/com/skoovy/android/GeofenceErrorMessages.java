package com.skoovy.android;

/**
 * Created by Lilith on 4/19/2017.
 */

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Geofence error codes mapped to error messages.
 */
public class GeofenceErrorMessages {
    /**
     * Prevents instantiation.
     */
    private GeofenceErrorMessages() {
    }

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();

        String geofence_not_available = "NA";
        String geofence_too_many_geofences = "too many";
        String geofence_too_many_pending_intents = "too many pending";
        String unknown_geofence_error = "unknown";

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return geofence_not_available;
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return geofence_too_many_geofences;
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return geofence_too_many_pending_intents;
            default:
                return unknown_geofence_error;
        }
    }
}
