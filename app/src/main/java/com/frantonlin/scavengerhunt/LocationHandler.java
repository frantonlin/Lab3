package com.frantonlin.scavengerhunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by keenan on 10/27/15.
 */

public class LocationHandler {

    public static final String TAG = LocationHandler.class.getSimpleName();
    private LocationCallback mLocationCallback;
    private LocationManager mLocationManager;

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
