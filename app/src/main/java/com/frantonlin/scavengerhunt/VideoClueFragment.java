package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
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
    TextView txtLat;
    Button checkLocationButton;

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
                LocationHandler handler = new LocationHandler(getActivity(), new LocationCallback() {
                    @Override
                    public void callback(Location location) {

                    }
                });
            }
        });


        locationHandler = new LocationHandler(getActivity(), new LocationCallback() {
            @Override
            public void callback(Location location) {


            }
        });

//        txtLat = (TextView) view.findViewById(R.id.textView);
//        txtLat.setText("Latitude:" + locationHandler.getLatitude() + ", Longitude:" + locationHandler.getLongitude());


        Uri uri=Uri.parse(testURL);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();

    }

}
