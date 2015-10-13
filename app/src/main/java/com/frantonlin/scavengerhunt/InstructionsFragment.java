package com.frantonlin.scavengerhunt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by keenan on 10/13/15.
 */
public class InstructionsFragment extends Fragment{

    public InstructionsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.instructions, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {

    }

}
