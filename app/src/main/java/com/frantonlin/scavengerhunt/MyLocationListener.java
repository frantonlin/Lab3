package com.frantonlin.scavengerhunt;

import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by keenan on 10/28/15.
 */

public class MyLocationListener implements LocationListener {

    public static final String TAG = LocationHandler.class.getSimpleName();

    @Override
    public void onLocationChanged(Location loc)
    {
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);
    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }
}


