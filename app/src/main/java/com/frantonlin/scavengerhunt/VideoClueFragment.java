package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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

    private double target_latitude = 42.2932;     // GOTTEN FROM DATABASE
    private double target_longitude = -71.2628;    // GOTTEN FROM DATABASE
    private double target_treshhold = 1;    // SET TO SOME NUMBER?

    Location currentLoc;

    private int clueNumber;  //WILL BE IN SHARED PREFERENCES?

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

        final Button checkLocationButton = (Button) view.findViewById(R.id.checkButton);


        // When user clicks 'CHECK' button, check if they are within range by locating their GPS
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
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); USE IF WANT LIVE UPDATING LOCATION STATUS
                    currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    checkLocation(currentLoc);
                    Log.d(TAG, "GPS: Latitude: " + String.valueOf(currentLoc.getLatitude()) + ", Longitude: " + String.valueOf(currentLoc.getLongitude()));
                } catch (SecurityException ex) {
                    Log.e("ERROR", ex.getMessage());
                }
            }
        });

        //Pares the video URL and play in the view, also include a media controller
        Uri uri=Uri.parse(testURL);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public void checkLocation(Location loc)
    {
        // Check for distance to the target given a specific latitude and longitude threshold of being within range
        if (Math.abs(loc.getLatitude() -  target_latitude) < target_treshhold && (Math.abs(loc.getLongitude() - target_longitude) < target_treshhold))
        {
            clueNumber += 1;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set title
            alertDialogBuilder.setTitle("CONGRATULATIONS!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Continue to next clue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // GO TO CAMERA INTENT THEN NEXT CLUE
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set title
            alertDialogBuilder.setTitle("NOT QUITE CLOSE ENOUGH!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Try Again!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CONTINUE WITH THE SAME VIEW
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    // On Success, go to take the photo before the next clue
    public interface onTakePhotoListener
    {
        public void takePhoto();
    }

}
