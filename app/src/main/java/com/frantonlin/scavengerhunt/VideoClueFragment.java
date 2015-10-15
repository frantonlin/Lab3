package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.content.Context;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Created by keenan on 10/13/15.
 */


public class VideoClueFragment extends Fragment {


    public VideoClueFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.clue, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        String LINK = "type_here_the_link";
//        setContentView(R.layout.mediaplayer);
//        VideoView videoView = (VideoView) findViewById(R.id.video);
//        MediaController mc = new MediaController(this);
//        mc.setAnchorView(videoView);
//        mc.setMediaPlayer(videoView);
//        Uri video = Uri.parse(LINK);
//        videoView.setMediaController(mc);
//        videoView.setVideoURI(video);
//        videoView.start();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {

    }

}
