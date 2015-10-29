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

    LocationListener locationListener;
    LocationManager locationManager;
    Location currentLoc;
    public static final String TAG = LocationHandler.class.getSimpleName();

    public LocationHandler(Location location, Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "GPS: Latitude: " + String.valueOf(location.getLatitude()) + ", Longitude: " + String.valueOf(location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };
        try {
            currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.d(TAG, "GPS: Latitude: " + String.valueOf(currentLoc.getLatitude()) + ", Longitude: " + String.valueOf(currentLoc.getLongitude()));
        } catch (SecurityException ex) {
            Log.e("SECURITY_ERROR", ex.getMessage());
        }

    }
}

//    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//    locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            Log.d(TAG, "GPS: Latitude: " + String.valueOf(location.getLatitude()) + ", Longitude: " + String.valueOf(location.getLongitude()));
//        }
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//    public void onProviderEnabled(String provider) {
//
//    }
//    public void onProviderDisabled(String provider) {
//
//    }
//};
//try {
//        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        checkLocation(currentLoc);
//        Log.d(TAG, "GPS: Latitude: " + String.valueOf(currentLoc.getLatitude()) + ", Longitude: " + String.valueOf(currentLoc.getLongitude()));
//        } catch (SecurityException ex) {
//        Log.e("ERROR", ex.getMessage());
//        }






