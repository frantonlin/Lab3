package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

        Uri uri=Uri.parse(testURL);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
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
