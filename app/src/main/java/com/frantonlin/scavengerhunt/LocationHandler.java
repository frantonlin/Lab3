package com.frantonlin.scavengerhunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by keenan on 10/28/15.
 */
public class LocationHandler {

    private LocationCallback callback;
    LocationListener locationListener;
    LocationManager locationManager;
    public static final String TAG = LocationHandler.class.getSimpleName();

    public LocationHandler(Context context, final LocationCallback callback1) {
        locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        callback = callback1;
        Log.d(TAG, "callback");
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                callback.callback(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {

            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Log.e("SECURITY_ERROR", ex.getMessage());
        }
    }
}
