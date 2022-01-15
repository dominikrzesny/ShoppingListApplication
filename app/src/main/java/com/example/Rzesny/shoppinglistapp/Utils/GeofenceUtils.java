package com.example.Rzesny.shoppinglistapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Rzesny.shoppinglistapp.GeofenceBroadcastReceiver;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GeofenceUtils extends ContextWrapper {

    public static GeofencingClient geofencingClient;
    public static PendingIntent pendingIntent;

    public GeofenceUtils(Context base) {
        super(base);
    }

    public static PendingIntent getPendingIntent(Activity activity){
        if(pendingIntent!=null){
            return pendingIntent;
        }
        Intent intent = new Intent(activity, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(activity,2607,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static GeofencingRequest getGeofencingRequest(List<Geofence> geofences){
        return new GeofencingRequest.Builder()
                .addGeofences(geofences)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public static Geofence getGeofence(String ID, LatLng latLng, int radius, int transitionTypes){

        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(10000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public static void createGeofencingClient(Activity activity){
        if(geofencingClient==null){
            if (checkUserLocationPermissions(activity)) {
                geofencingClient = LocationServices.getGeofencingClient(activity);
            }
        }
    }

    public static boolean checkUserLocationPermissions(Activity activity){
        if(ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    public static Location getLastLocation(Activity activity){
        LocationManager mLocationManager = (LocationManager)activity.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            try{
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            catch (SecurityException se){
                Log.d("GeofenceUtils", "getLastLocation: " + se.getMessage());
            }
        }
        return bestLocation;
    }

    public static void requestLocationsPermissions(Activity activity){

        String[] perms = {
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        int requestCode = 1;
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(perms, requestCode);
        }
    }
}
