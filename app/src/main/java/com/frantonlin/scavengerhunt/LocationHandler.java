package com.frantonlin.scavengerhunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
//import com.google.android.gms.location.LocationRequest;

/**
 * Created by keenan on 10/27/15.
 */
public class LocationHandler {

    public static final String TAG = LocationHandler.class.getSimpleName();
    private LocationCallback mLocationCallback;
    private LocationManager mLocationManager;
//    private LocationRequest mLocationRequest;

    private double latitude;
    private double longitude;

    public LocationHandler(Context context, LocationCallback callback)
    {
        mLocationCallback = callback;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Log.d(TAG,"GPS Location Requested" );
        } catch (SecurityException ex) {

            Log.e("ERROR: ", ex.getMessage());
        }

//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(4 * 1000)         // 4 seconds, in milliseconds
//                .setFastestInterval(3 * 1000); // 3 seconds, in milliseconds
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLocationCallback.callback(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
