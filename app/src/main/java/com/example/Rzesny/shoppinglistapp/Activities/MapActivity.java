package com.example.Rzesny.shoppinglistapp.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.Rzesny.shoppinglistapp.GeofenceBroadcastReceiver;
import com.example.Rzesny.shoppinglistapp.Models.Shop;
import com.example.Rzesny.shoppinglistapp.R;
import com.example.Rzesny.shoppinglistapp.Utils.GeofenceUtils;
import com.example.Rzesny.shoppinglistapp.Utils.UserUtils;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Shop> shops = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GeofenceUtils.createGeofencingClient(this);
    }

    @Override
    protected void onDestroy() {
        GeofenceUtils.geofencingClient.removeGeofences(GeofenceUtils.getPendingIntent(this)); //clear all geofences when u exit mapActivity
        Log.d(TAG, "onDestroy: zamykam MapActivity");
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        Location location = GeofenceUtils.getLastLocation(this);
        if(location!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),14));
        }
        GeofenceUtils.geofencingClient.removeGeofences(GeofenceUtils.getPendingIntent(this)); //clear all geofences added before
        addGeofences();
    }

    private void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(Color.argb(64,255,0,0));
        mMap.addCircle(circleOptions);
    }

    public void addGeofences(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://shoppinglistapp-fe3b0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users/"+ UserUtils.loggedUser.getDisplayName()+"/Shops");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Geofence> geofences = new ArrayList<Geofence>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Shop shop = dataSnapshot.getValue(Shop.class);
                    shops.add(shop);
                    LatLng coordinates = new LatLng(shop.latitude, shop.longitude);
                    Geofence geofence = GeofenceUtils.getGeofence(shop.shopName,coordinates,shop.radius,Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL);
                    geofences.add(geofence);
                    mMap.addMarker(new MarkerOptions().position(coordinates).title(shop.shopName));
                    addCircle(coordinates,shop.radius);
                }
                if(geofences.isEmpty()){
                    return;
                }
                GeofencingRequest geofencingRequest = GeofenceUtils.getGeofencingRequest(geofences);
                PendingIntent pendingIntent = GeofenceUtils.getPendingIntent(MapActivity.this);
                try{
                    GeofenceUtils.geofencingClient.addGeofences(geofencingRequest,pendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Log.d("MainActivity","onSuccess: Geofences added "+ geofences.size());
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MainActivity","onFailure: "+ e.getMessage());
                        }
                    });

                }
                catch(SecurityException se){
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}