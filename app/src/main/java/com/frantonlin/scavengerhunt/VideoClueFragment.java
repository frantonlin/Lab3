package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by keenan on 10/13/15.
 */


public class VideoClueFragment extends Fragment {

//    String testURL = "https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp";
    onTakePhotoListener mTakePhotoListener;
    double latitude;
    double longitude;

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
        Button takePhoto = (Button)view.findViewById(R.id.checkPos);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoListener.takePhoto();
            }
        });

        getClueInfoWithCallback();
    }

    public void getClueInfoWithCallback() {
        ((MainActivity) getActivity()).getHttpHandler().getInfo(new InfoCallback() {
            @Override
            public void callback(boolean success, HashMap<String, String> clueInfo) {
                if (success) {
                    Log.d("Success", Boolean.toString(success));
                    loadVideo("https://s3.amazonaws.com/olin-mobile-proto/" + clueInfo.get("s3id"));
                    TextView clueLabel = (TextView) getActivity().findViewById(R.id.clueLabel);
                    clueLabel.setText("Clue " + clueInfo.get("id") + "/" + clueInfo.get("numClues"));
                    latitude = Double.parseDouble(clueInfo.get("latitude"));
                    longitude = Double.parseDouble(clueInfo.get("longitude"));
                } else {
                    Log.d("Failure", Boolean.toString(success));
                }
            }
        }, ((MainActivity) getActivity()).getClueNum());
    }

    public void loadVideo(String videoUrl) {
        Uri uri=Uri.parse(videoUrl);
        VideoView videoView = (VideoView) getActivity().findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public interface onTakePhotoListener
    {
        public void takePhoto();
    }

}
