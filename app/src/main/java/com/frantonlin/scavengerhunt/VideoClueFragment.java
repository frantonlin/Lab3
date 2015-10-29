package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

/**
 * Created by keenan on 10/13/15.
 */


public class VideoClueFragment extends Fragment {

    String testURL = "https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp";
    onTakePhotoListener mTakePhotoListener;
    public static final String TAG = VideoClueFragment.class.getSimpleName();
    public LocationManager locationManager;
    public LocationListener locationListener;

    private double actual_latitude;
    private double actual_longitude;
    private double target_latitude;
    private double target_longitude;
    private double target_treshhold; // SET TO SOME NUMBER?



    public VideoClueFragment(){

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mTakePhotoListener = (onTakePhotoListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.clue, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        Button takePhoto = (Button)view.findViewById(R.id.testButton);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoListener.takePhoto();
            }
        });

        Button checkLocationButton = (Button) view.findViewById(R.id.checkButton);

        checkLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
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
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                } catch (SecurityException ex) {
                    Log.e("ERROR", ex.getMessage());
                }

            }
        });


        Uri uri=Uri.parse(testURL);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();

    }

    public void checkLocation(Location loc)
    {
        if (Math.abs(loc.getLatitude() -  target_latitude) < target_treshhold)
        {
            //GO TO NEXT CLUE?
        }
    }

    public interface onTakePhotoListener
    {
        public void takePhoto();
    }

}
