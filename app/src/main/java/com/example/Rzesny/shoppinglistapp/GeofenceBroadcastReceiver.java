package com.example.Rzesny.shoppinglistapp;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.Rzesny.shoppinglistapp.Services.NotificationService;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            List<Geofence> triggerdGeofences = geofencingEvent.getTriggeringGeofences();

            for(Geofence geofence : triggerdGeofences){
                context.startService(
                        new Intent(context, NotificationService.class)
                        .putExtra("ShopName",geofence.getRequestId())
                        .putExtra("TransitionType","ENTER"));
            }
        }
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List<Geofence> triggerdGeofences = geofencingEvent.getTriggeringGeofences();
            for(Geofence geofence : triggerdGeofences){
                context.startService(
                        new Intent(context, NotificationService.class)
                        .putExtra("ShopName",geofence.getRequestId())
                        .putExtra("TransitionType","EXIT"));
            }
        }
    }
}
