package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by keenan on 10/13/15.
 */
public class InstructionsFragment extends Fragment{

    Button ready;
    onClueListener mClueListener;


    public InstructionsFragment(){

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mClueListener = (onClueListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.instructions, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        ready = (Button)view.findViewById(R.id.button2);

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClueListener.onClue();
            }
        });
    }

    public interface onClueListener
    {
        public void onClue();
    }

}
