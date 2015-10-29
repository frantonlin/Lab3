package com.frantonlin.scavengerhunt;

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
    public LocationHandler locationHandler;
    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    TextView txtLat;
    Button checkLocationButton;
    Location location;

    public VideoClueFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.clue, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        checkLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyLocationListener handler = new MyLocationListener();
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, handler);
                } catch (SecurityException ex){
                    Log.e("ERROR: ", ex.getMessage());
                }
//                txtLat = (TextView) view.findViewById(R.id.textView);
//                txtLat.setText("Latitude:" + handler.getLatitude() + ", Longitude:" + handler.getLongitude());




            }
        });


        locationHandler = new LocationHandler(getActivity(), new LocationCallback() {
            @Override
            public void callback(Location location) {


            }
        });

        Uri uri=Uri.parse(testURL);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();

    }

}
